package binaryTree;
import java.util.ArrayList;
import java.util.Stack;
 
/**
 * 二叉树类
 * 拥有属性为root，其构建后为数的根节点
 */
class BinaryNode{
	private String data;
	private BinaryNode left;
	private BinaryNode right;
	public BinaryNode(String data) {
	    this.data = data;
	}
	public String getData() {
		return this.data;
	}
	public void setLeft(BinaryNode left) {
		this.left = left;
	}
	public void setRight(BinaryNode right) {
		this.right = right;
	}
	public BinaryNode getLeft() {
		return left;
	}
	public BinaryNode getRight() {
		return right;
	}

}
public class Manage {
    public static void main(String[] args) {
        //创建binaryTree对象，其初始包含data为null的root节点
        BinaryTree binaryTree = new BinaryTree();
        //build二叉树时，需要输入表达式的String
        binaryTree.build("45+(23*56+2)/2-5");
        //查看是否构建完成，获取现在的root节点
        System.out.println("二叉树的root为: "+binaryTree.getRoot().getData());
        binaryTree.output(1);
        binaryTree.output(2);
        binaryTree.output(3);
    }
    
	public static double caculatePloenTree(BinaryNode root){
		  if(!(root.getData().equals("+") || root.getData().equals("-")||root.getData().equals("*")||root.getData().equals("-")))
				return Double.parseDouble(root.getData());
		  else if(root.getData().equals("+")) {
			  return caculatePloenTree(root.getLeft()) + caculatePloenTree(root.getRight());
		  }
		  else if(root.getData().equals("-")){
			  return caculatePloenTree(root.getLeft()) - caculatePloenTree(root.getRight());
		  }
		  else if(root.getData().equals("*")){
			  return caculatePloenTree(root.getLeft()) * caculatePloenTree(root.getRight());
		  }
		  else{
			  return caculatePloenTree(root.getLeft()) / caculatePloenTree(root.getRight());
		  }
	}
}

class BinaryTree {
    //根节点
    private BinaryNode root = new BinaryNode(null);
    public BinaryNode getRoot() {
        return root;
    }
 
 
    //—————————————————————————————————创建 和 输出 1 ———————————————————————————————————————
 
    /**
     * 创建表达式二叉树
     * @param str ：输入为字符串
     */
    public void build(String str) {
        // numbers存储数字和节点，operations存储运算符号
        // binaryNode用于构建数，snum用于接受个位十位百位数字
        ArrayList<BinaryNode> numbers = new ArrayList<>();
        ArrayList<BinaryNode> operations = new ArrayList<>();
        BinaryNode binaryNode;
        String snum = "";
 
        //1.遍历str，找出所有的运算符和数字，存入numbers和operations数组队列
        for (int i = 0; i < str.length(); i++) {
            //1.1 取出字符串的各个字符
            char ch = str.charAt(i);
            //1.2 判断为符号还是数字，若为数字，则将s+=ch（防止数字为十位百位数）
            if (ch >= '0' && ch <= '9') {
                snum += ch + "";
            }
            //1.3 若为运算符，则将s和ch分别放入numbers、operations数组队列
            else {
                numbers.add(new BinaryNode(snum));
                operations.add(new BinaryNode(ch + " "));
                snum = "";
            }
        }
        //1.4 将最后一位数字放入numbers数组队列
        numbers.add(new BinaryNode(snum));
 
 
        //2. 循环构建树，直至operations队列为空结束
        while(!operations.isEmpty()) {
            // 2.1从运算符中取出第一个作为node的数据；
            binaryNode = operations.get(0);
            operations.remove(0);
            //2.2从数字取出第一个、第二个作为左、右；
            binaryNode.setLeft(numbers.get(0));
            binaryNode.setRight(numbers.get(1));
            numbers.remove(0);
            numbers.remove(0);
            //2.3构建node,将其作为根节点root放回数字列表
            root = binaryNode;
            numbers.add(0, binaryNode);
        }
    }
 
 
    /**
     * 选择方式遍历输出表达式二叉树
     * @param i：1——先序 2——中序 3——后序
     */
    public void output(int i) {
        switch (i) {
            case 1:
                System.out.println("输出——先序遍历：");
                preOrder(root);
                System.out.println("");
                break;
            case 2:
                System.out.println("输出——中序遍历：");
                midOrder(root);
                System.out.println("");
                break;
            case 3:
                System.out.println("输出——后序遍历：");
                posOrder(root);
                System.out.println("");
                break;
        }
 
    }
 
 
    //—————————————————————————————————遍历 3———————————————————————————————————————
 
 
    /**
     * 递归方法 —— 前序遍历的规则：
     * （1）访问根节点
     * （2）前序遍历左子树
     * （3）前序遍历右子树
     */
    public void preOrder(BinaryNode node) {
        if (node != null) {
            System.out.print(node.getData() + " ");
            preOrder(node.getLeft());
            preOrder(node.getRight());
        }
    }
 
    /**
     * 递归方法 —— 中序遍历的规则：
     * （1）中序遍历左子树
     * （2）访问根节点
     * （3）中序遍历右子树
     */
    public void midOrder(BinaryNode node) {
        if (node != null) {
            midOrder(node.getLeft());
            System.out.print(node.getData() + " ");
            midOrder(node.getRight());
        }
    }
 
 
    /**
     * 递归方法 —— 后序遍历的规则：
     * （1）后序遍历左子树
     * （2）后序遍历右子树
     * （3）访问根节点
     */
    public void posOrder(BinaryNode node) {
        if (node != null) {
            posOrder(node.getLeft());
            posOrder(node.getRight());
            System.out.print(node.getData() + " ");
        }
    }
 
 
    /**
     * 非递归方法 —— 前序遍历的规则：
     * （1）访问根节点
     * （2）前序遍历左子树
     * （3）前序遍历右子树
     */
    public void preOrder2() {
        BinaryNode node = root;
        Stack<BinaryNode> stack = new Stack<>();
        ArrayList<String> preList = new ArrayList<>();
 
 
        while (node != null || stack.size() != 0) {
            while (node != null) {
                stack.push(node);
                preList.add(node.getData());
 
                node = node.getLeft();
            }
            if (stack.size() != 0) {
                node = stack.pop();
                node = node.getRight();
            }
        }
        System.out.println("非递归——先序遍历:" + preList.toString());
 
    }
 
    /**
     * 非递归方法 —— 中序遍历的规则：
     * （1）中序遍历左子树
     * （2）访问根节点
     * （3）中序遍历右子树
     */
    public void midOrder2() {
        Stack<BinaryNode> stack = new Stack<>();
        ArrayList<String> midList = new ArrayList<>();
        BinaryNode node = root;
        while (node != null || stack.size() != 0) {
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }
            if (stack.size() != 0) {
                node = stack.pop();
                midList.add(node.getData());
                node = node.getRight();
            }
        }
        System.out.println("非递归——中序遍历： " + midList.toString());
    }
 
 
    //—————————————————————————————————测试 2———————————————————————————————————————
 
    /**
     * 输出检验函数，查看numbers和operations是否已经存入数据
     * @param list：Node列表
     */
    public void printList(ArrayList<BinaryNode> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
            System.out.print("    ");
        }
    }
 
    /**
     * 输出检验函数，Stack是否已经存入数据
     * @param list：Node列表
     */
    public void printList(Stack<BinaryNode> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i).getData());
            System.out.print("    ");
        }
    }
 
}