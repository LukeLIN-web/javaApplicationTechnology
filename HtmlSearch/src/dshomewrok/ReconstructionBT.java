package dshomewrok;

import java.util.HashMap;

class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
}


public class ReconstructionBT {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        int root = preorder[0];
        TreeNode tn = new TreeNode(root);
        if(preorder.length == 1){
            return tn;
        }
        HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();
        int i = 0;
        while(i < inorder.length){
			m.put(inorder[i],i);//from value find the index
			i++;
		}
        int pivot = m.get(root);
        System.arraycopy(preorder,);//System.arraycopy(源数组名称，源数组开始点，目标数组名称，目标数组开始点，拷贝长度);
        return resu(0,pivot, preorder.length-1);
    }
    public TreeNode resu(int left, int right) {
    	
    }
    	       
}
