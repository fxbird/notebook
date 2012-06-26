package test;

import java.awt.datatransfer.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

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
public class DnDTreeList implements Transferable, Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1270874212613332692L;
	/**
	 * Data FLAVOR that allows a DnDTreeList to be extracted from a transferable
	 * object
	 */
	public final static DataFlavor DnDTreeList_FLAVOR = new DataFlavor(DnDTreeList.class,
			"Drag and drop list");
	/**
	 * List of flavors this DnDTreeList can be retrieved as. Currently only
	 * supports DnDTreeList_FLAVOR
	 */
	protected static DataFlavor[] flavors = { DnDTreeList.DnDTreeList_FLAVOR };

	/**
	 * Nodes to transfer
	 */
	protected ArrayList<TreePath> nodes;

	/**
	 */
	public DnDTreeList(ArrayList<TreePath> nodes)
	{
		this.nodes = nodes;
	}

	public ArrayList<TreePath> getNodes()
	{
        System.out.println("nodes:"+nodes.get(0).getClass()+"@"+nodes.get(0).hashCode());

		return this.nodes;
	}

	/**
	 * @param flavor
	 * @return
	 * @throws java.awt.datatransfer.UnsupportedFlavorException
	 * @throws java.io.IOException
	 **/
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		if (this.isDataFlavorSupported(flavor))
		{
			return this;
		}
		else
		{
			throw new UnsupportedFlavorException(flavor);
		}
	}

	/**
	 * @return
	 **/
	public DataFlavor[] getTransferDataFlavors()
	{
		// TODO Auto-generated method stub
		return DnDTreeList.flavors;
	}

	/**
	 * @param flavor
	 * @return
	 **/
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		DataFlavor[] flavs = this.getTransferDataFlavors();
		for (int i = 0; i < flavs.length; i++)
		{
			if (flavs[i].equals(flavor))
			{
				return true;
			}
		}
		return false;
	}

}
