package com.students.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换符
    private static final String REPLACEMENT = "***";

    // 根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init(){
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-word.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyWord;
            while ((keyWord=reader.readLine()) != null){
                // 添加到前缀树
                this.addKeyWord(keyWord);
            }
        }catch (IOException e){
            logger.error("加载敏感词文件失败!");
        }
    }

    // 添加敏感词到前缀树
    private void addKeyWord(String keyWord){
        TrieNode tempNode = rootNode;
        for(int i = 0; i < keyWord.length(); i++){
            char c = keyWord.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null){
                // 初始化新的子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            // 指向子节点，进入下一轮
            tempNode = subNode;

            // 设置结束标识
            if(i == keyWord.length()-1){
                tempNode.setKeyWords(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text))return null;
        // 指针1
        TrieNode tempNode = rootNode;
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        while(position <text.length()){
            char c = text.charAt(position);

            // 跳过符号
            if(isSymbol(c)){
                // 若指针1处于根节点，将此符号计入结果，指针2向下走一步
                if(tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头还是中间，指针3都会向下走一步
                position++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                // 以begin为开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                position = ++begin;
                // 指针1重新指向根节点
                tempNode = rootNode;
            }else if(tempNode.isKeyWords()){
                // 发现敏感词
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                // 指针1重新指向根节点
                tempNode = rootNode;
            }else{
                // 检查下一个字符
                position++;
            }
        }

        // 将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    private boolean find(String text){
        TrieNode node = rootNode;
        int begin = 0;
        while(begin < text.length() && !node.isKeyWords){
            TrieNode tempNode = node.getSubNode(text.charAt(begin));
            if(tempNode == null)return false;
            node = tempNode;
            begin++;
        }
        return node.isKeyWords ? true : false;
    }

    // 判断是否为符号
    private boolean isSymbol(Character c){
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }


    // 前缀树
    private class TrieNode{
        // 关键词结束标识
        private boolean isKeyWords = false;

        // 子节点(key下级字符，value下级节点）
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeyWords() {
            return isKeyWords;
        }

        public void setKeyWords(boolean keyWords) {
            isKeyWords = keyWords;
        }

        // 添加子节点
        public void addSubNode(Character c, TrieNode node){
            subNodes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }


}
