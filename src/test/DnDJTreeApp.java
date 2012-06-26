package test;

import javax.swing.*;

public class DnDJTreeApp
{
	public static void main(String[] args)
	{
		DnDNode root = new DnDNode("root");
		DnDNode child = new DnDNode("parent 1");
		root.add(child);
		child = new DnDNode("parent 2");
		root.add(child);
		child = new DnDNode("parent 3");
		child.add(new DnDNode("child 1"));
		child.add(new DnDNode("child 2"));
		root.add(child);
		DnDJTree tree = new DnDJTree(root);
		JFrame frame = new JFrame("Drag and drop JTrees");
		frame.getContentPane().add(tree);
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
