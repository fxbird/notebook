/*
 * FrmFavorite.java
 *
 * Created on __DATE__, __TIME__
 */

package frame;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.lf5.util.DateFormatManager;

import xdg.XdgUtil;

import bean.FavoriteItem;
import bo.BookBO;

/**
 *
 * @author  __USER__
 */
public class FrmFavorite extends javax.swing.JFrame {
	private BookBO bo;
	private JPopupMenu popMenu;

	/** Creates new form FrmFavorite */
	public FrmFavorite(BookBO bookBO) {
		this.bo = bookBO;
		initComponents();

		readFavorite();
		setPopMenu();
		setRightMenu();
		

		setSize(800, 650);
		setVisible(true);
	}

	private void readFavorite() {
		FavoriteTableModel model = new FavoriteTableModel(bo
				.getFavoriteItemList());
		tblFavorite.setModel(model);
        XdgUtil.setCellWidth(tblFavorite,0,50);
        XdgUtil.setCellWidth(tblFavorite,1,60);
//        XdgUtil.setCellWidth(tblFavorite,3,100);
//        XdgUtil.setCellWidth(tblFavorite,4,100);
	}
	
	private void setPopMenu(){
		popMenu=new JPopupMenu();
		popMenu.add(new AbstractAction("Delete these selected favoite item") {
			public void actionPerformed(ActionEvent e) {
				if (tblFavorite.getSelectedRowCount()==0) return;
				if (XdgUtil.showConfirm(FrmFavorite.this, "Really delete?")==1){
					int[] rows=tblFavorite.getSelectedRows();
					for (int i = rows.length-1; i >=0; i--) {
                        int r=rows[i];
						bo.deleteFavoriteItem((Integer)tblFavorite.getValueAt(r, 1));
					}
					
					readFavorite();
				}
				
			}
		});
	}
	
	private void setRightMenu(){
		tblFavorite.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()){
					popMenu.show(tblFavorite, e.getX(), e.getY());
				}
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		tblFavorite = new javax.swing.JTable();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Manage Favorite");

		tblFavorite.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null, null },
						{ null, null, null, null }, { null, null, null, null },
						{ null, null, null, null } }, new String[] { "Title 1",
						"Title 2", "Title 3", "Title 4" }));
		jScrollPane1.setViewportView(tblFavorite);

		getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable tblFavorite;
	// End of variables declaration//GEN-END:variables
}

class FavoriteTableModel extends AbstractTableModel {
	private List<FavoriteItem> data;
	private String[] colNames = {"No.", "Note No.", "Title","Type","Drop Date" };

	public FavoriteTableModel(List<FavoriteItem> data) {
		this.data = data;
	}

	@Override
	public String getColumnName(int column) {

		return colNames[column];
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		FavoriteItem item = data.get(row);
		
		switch (col) {
		case 0:
			return row+1;
		case 1:
			return item.getNote().getId();
		case 2:
			return item.getNote().getTitle();
		case 3:
			return item.getType().getName();
		case 4:
			return DateFormatUtils.format(item.getDropDate(),"yyyy/MM/dd kk:mm:ss");
		default:
			return null;
		}
	}

}