package com.luwang;

import java.util.Stack;

public class Calculator {

    private Stack<Integer> intStack;
    private Stack<Character> operatorStack;

    public int calculate(String expression) {
        expression = removeStringAllSpace(expression);

        expression += addEqualSignIfAbsent(expression);

        // 检查表达式是否合法
        if (!isStandard(expression)) {
            System.err.println("错误：算术表达式有误！");
            return 0;
        }
        // 初始化栈
        intStack = new Stack<>();
        operatorStack = new Stack<>();
        // 用于缓存数字，因为数字可能是多位的
        StringBuffer temp = new StringBuffer();
        // 从表达式的第一个字符开始处理
        for (int i = 0; i < expression.length(); i++) {
            char eachChar = expression.charAt(i); // 获取一个字符
            if (isNumber(eachChar)) { // 若当前字符是数字
                temp.append(eachChar); // 加入到数字缓存中
            } else { // 非数字的情况
                String tempStr = temp.toString(); // 将数字缓存转为字符串
                if (!tempStr.isEmpty()) {
                    int num = Integer.parseInt(tempStr); // 将数字字符串转为长整型数
                    intStack.push(num); // 将数字压栈
                    temp = new StringBuffer(); // 重置数字缓存
                }
                // 判断运算符的优先级，若当前优先级低于栈顶的优先级，则先把计算前面计算出来
                while (!comparePri(eachChar) && !operatorStack.empty()) {
                    int b = intStack.pop(); // 出栈，取出数字，后进先出
                    int a = intStack.pop();
                    // 取出运算符进行相应运算，并把结果压栈进行下一次运算
                    switch (operatorStack.pop()) {
                        case '+':
                            intStack.push(a + b);
                            break;
                        case '-':
                            intStack.push(a - b);
                            break;
                        case '*':
                            intStack.push(a * b);
                            break;
                        case '/':
                            intStack.push(a / b);
                            break;
                        default:
                            break;
                    }
                }
                if (eachChar != '=') {
                    operatorStack.push(eachChar); // 符号入栈
                    if (eachChar == ')') { // 去括号
                        operatorStack.pop();
                        operatorStack.pop();
                    }
                }
            }
        }
        return intStack.pop();
    }

    private String removeStringAllSpace(String str) {
        return str != null ? str.replaceAll(" ", "") : "";
    }

    private String addEqualSignIfAbsent(String expression) {
        return (expression.length() > 1 && getLastChar(expression) != '=') ? "=" : "";
    }

    private char getLastChar(String expression) {
        return expression.charAt(expression.length() - 1);
    }

    private boolean isStandard(String expression) {
        if (isNullOrEmpty(expression)) // 表达式不能为空
            return false;
        Stack<Character> stack = new Stack<>(); // 用来保存括号，检查左右括号是否匹配
        boolean hasMultipleEquals = false; // 用来标记'='符号是否存在多个
        for (int i = 0; i < expression.length(); i++) {
            char eachChar = expression.charAt(i);
            // 判断字符是否合法
            if (!isValidExprChar(eachChar)) {
                return false;
            }
            // 将左括号压栈，用来给后面的右括号进行匹配
            if (isOpeningBracket(eachChar)) {
                stack.push(eachChar);
            }
            if (isClosingBracket(eachChar)) { // 匹配括号
                if (stack.isEmpty() || !isOpeningBracket(stack.pop())) // 括号是否匹配
                    return false;
            }
            // 检查是否有多个'='号
            if (eachChar == '=') {
                if (hasMultipleEquals)
                    return false;
                hasMultipleEquals = true;
            }
        }
        // 可能会有缺少右括号的情况
        if (!stack.isEmpty())
            return false;
        // 检查'='号是否不在末尾
        return getLastChar(expression) == '=';
    }

    private boolean isValidExprChar(char c) {
        return isNumber(c) || isBracket(c) || isOperator(c);
    }

    private boolean isNumber(char c) {
        return Character.isDigit(c);
    }

    private boolean isBracket(char c) {
        return isOpeningBracket(c) || isClosingBracket(c);
    }

    private boolean isOpeningBracket(char c) {
        return c == '(';
    }

    private boolean isClosingBracket(char c) {
        return c == ')';
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '=';
    }

    private boolean isNullOrEmpty(String expression) {
        return expression == null || expression.isEmpty();
    }

    /**
     * 比较优先级：如果当前运算符比栈顶元素运算符优先级高则返回true，否则返回false
     */
    private boolean comparePri(char symbol) {
        if (operatorStack.empty()) { // 空栈返回ture
            return true;
        }

        char top = operatorStack.peek(); // 查看堆栈顶部的对象，注意不是出栈
        if (top == '(') {
            return true;
        }
        // 比较优先级
        switch (symbol) {
            case '(': // 优先级最高
                return true;
            case '*': {
                // 优先级比+和-高
                return top == '+' || top == '-';
            }
            case '/': {
                // 优先级比+和-高
                return top == '+' || top == '-';
            }
            case '+':
                return false;
            case '-':
                return false;
            case ')': // 优先级最低
                return false;
            case '=': // 结束符
                return false;
            default:
                break;
        }
        return true;
    }
}