/*
 * FrmImport.java
 *
 * Created on __DATE__, __TIME__
 */

package frame;

import action.TypeCallback;
import bean.Note;
import bean.NoteType;
import bo.BookBO;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import other.MyUtil;
import other.PropHelper;
import xdg.XdgUtil;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author  __USER__
 */
public class FrmImport extends javax.swing.JFrame {
	public static final String KEY_IMPORT_PATH = "import.path";
	private PropertiesConfiguration pc = PropHelper.getPropConfigInstance();
	private BookBO bookBO;
	private ImportTableModel importTableModel;
	private List<Note> notes;

	/** Creates new form FrmImport */
	public FrmImport(BookBO bookBO) {
		initComponents();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.bookBO = bookBO;

	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		tblImport = new javax.swing.JTable();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Import Data");

		jLabel1.setText("Records to import");

		tblImport.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null, null },
						{ null, null, null, null }, { null, null, null, null },
						{ null, null, null, null } }, new String[] { "Title 1",
						"Title 2", "Title 3", "Title 4" }));
		jScrollPane1.setViewportView(tblImport);

		jButton1.setText("Select Target NoteType");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jButton2.setText("Select Source Data");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		jButton3.setText("Exit");
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jScrollPane1,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														1019, Short.MAX_VALUE)
												.addGroup(
														layout.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(
																		jLabel1,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		146,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGroup(
																		javax.swing.GroupLayout.Alignment.TRAILING,
																		layout.createSequentialGroup()
																				.addComponent(
																						jButton2)
																				.addGap(32,
																						32,
																						32)
																				.addComponent(
																						jButton1)
																				.addGap(47,
																						47,
																						47)
																				.addComponent(
																						jButton3,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						96,
																						javax.swing.GroupLayout.PREFERRED_SIZE)
																				.addGap(78,
																						78,
																						78))))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jButton3)
												.addComponent(jButton1)
												.addComponent(jButton2))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(jLabel1)
								.addGap(6, 6, 6)
								.addComponent(jScrollPane1,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										667, Short.MAX_VALUE).addContainerGap()));

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	/**
	 * select target type button
	 * @param evt
	 */
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		TypeCallback callback = new TypeCallback() {
			public void handle(NoteType type) {
				int[] selected = tblImport.getSelectedRows();
				if (selected.length == 0) {
					XdgUtil.showMsg(null, "Select records first");
					return;
				}
				for (int i = selected.length - 1; i >= 0; i--) {
					Note note = importTableModel.getNotes().get(selected[i]);
					note.setTypeId(type.getId());
					bookBO.addNote(note);
				}

				for (int i = selected.length - 1; i >= 0; i--) {
					importTableModel.getNotes().remove(selected[i]);
				}

				renderTable(importTableModel.getNotes());
			}
		};

		DlgType dlgType = new DlgType(FrmImport.this, true, bookBO, callback);
		dlgType.setSize(400, 600);
		dlgType.setVisible(true);
		XdgUtil.displayInMiddle(dlgType);

	}

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
		String lastPath = pc
				.getString(KEY_IMPORT_PATH, MyUtil.getDesktopPath());
		JFileChooser chooser = new JFileChooser(lastPath);
		int rst = chooser.showOpenDialog(this);
		if (rst != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = chooser.getSelectedFile();
		try {
			if (file != null) {
				pc.setProperty(KEY_IMPORT_PATH, file.getPath());
				pc.save();
				notes = (List<Note>) MyUtil.deserialize(file.getPath());
				renderTable(notes);
			}
		} catch (ConfigurationException e) {
			e.printStackTrace();
			XdgUtil.showError(null, e);
		} catch (IOException e) {
			e.printStackTrace();
			XdgUtil.showError(null, e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			XdgUtil.showError(null, e);
		}

	}

	private void renderTable(List<Note> notes) {
		ImportTableModel tableModel = new ImportTableModel(notes);
		tblImport.setModel(tableModel);
		this.importTableModel = tableModel;
		XdgUtil.setCellWidth(tblImport, 0,50);
	}

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
		this.dispose();
	}

	/**
	 * @param args the command line arguments
	 */

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable tblImport;
	// End of variables declaration//GEN-END:variables

}

class ImportTableModel extends AbstractTableModel {
	private List<Note> notes;
	private String[] colNames = { "No.", "Title", "NoteType", "Creation Time" };

	ImportTableModel(List<Note> notes) {
		this.notes = notes;
	}

	public int getRowCount() {
		return notes.size();
	}

	public int getColumnCount() {
		return 4;
	}

	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return row + 1;
		case 1:
			return notes.get(row).getTitle();
		case 2:
			return notes.get(row).getTypeName();
		case 3:
			return notes.get(row).getTs();
		}
		return null;
	}

	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	public List<Note> getNotes() {
		return notes;
	}
}