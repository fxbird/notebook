package test;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * @author helloworld922
 *         <p>
 * @version 1.0
 *          <p>
 *          copyright 2010 <br>
 *
 *          You are welcome to use/modify this code for any purposes you want so
 *          long as credit is given to me.
 */
public class DnDJTree extends JTree implements TreeSelectionListener
{
	/**
	 *
	 */
	private static final long serialVersionUID = -4260543969175732269L;
	/**
	 * Used to keep track of selected nodes. This list is automatically updated.
	 */
	protected ArrayList<TreePath> selected;

	/**
	 * Constructs a DnDJTree with root as the main node.
	 *
	 * @param root
	 */
	public DnDJTree(TreeNode root)
	{
		super(root);
		// turn on the JComponent dnd interface
		this.setDragEnabled(true);
		// setup our transfer handler
		this.setTransferHandler(new JTreeTransferHandler(this));
		// setup tracking of selected nodes
		this.selected = new ArrayList<TreePath>();
		this.addTreeSelectionListener(this);
	}

	/**
	 * @return the list of selected nodes
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TreePath> getSelection()
	{  ArrayList<TreePath> clone=  (ArrayList<TreePath>) (this.selected).clone();
        System.out.println(this.selected);
        System.out.println(clone);
        System.out.println("=====");
		return clone;
	}

	/**
	 * keeps the list of selected nodes up to date.
	 *
	 * @param e
	 **/
	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		// should contain all the nodes who's state (selected/non selected)
		// changed
		TreePath[] selection = e.getPaths();
		for (int i = 0; i < selection.length; i++)
		{
			// for each node who's state changed, either add or remove it form
			// the selected nodes
			if (e.isAddedPath(selection[i]))
			{
				// node was selected
				this.selected.add(selection[i]);
			}
			else
			{
				// node was de-selected
				this.selected.remove(selection[i]);
			}
		}
	}
}
