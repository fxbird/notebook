/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrmMain.java
 *
 * Created on Feb 23, 2010, 7:49:07 PM
 */
package frame;

import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;

import action.TypeCallback;
import bean.FavoriteNote;
import bean.NoteType;
import com.xdg.util.*;
import dao.ibatis.FavoriteDAOIbts;
import dao.ibatis.NoteDAOIbts;
import dao.ibatis.NoteTypeDAOIbts;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import other.*;

import javax.swing.*;

import tree.TypeTreeWrapper;
import util.ConfigParser;
import vetoselection.VetoTreeSelectionListener;
import vetoselection.VetoTreeSelectionModel;
import xdg.XdgUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.lang.reflect.InvocationTargetException;

import bean.Note;
import bo.BookBO;
import dbwin.DbWinModel;
import dbwin.render.TypeRender;
import dbwin.editor.TypeEditor;
import db.DBConnection;

import java.io.File;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.DefaultHighlighter;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


/**
 * @author fxbird
 */
public class FrmMainI extends JFrame {

    private NoteDAO noteDAO;
    private TypeDAO typeDAO;
    private FavoriteDAOIbts favDAO;
    private BookBO bookBO;
    private List<Note> searchedNotes;
    private NoteTableModel2 currentModel;
    private boolean isInFocus;
    private List listTypes;
    private TypeEditor typeEditor;
    private TypeRender typeRender;
    private boolean painting = true;
    private CommonAction commonSearch;
    private ActionListener actOpen;
    private ActionListener actTypeMaintain;
    private JPopupMenu popTypeTree;
    private JPopupMenu popTable;
    private JMenu itemHiddenTypes;
    private Matcher matcher;
    private boolean isMin;
    private int leftSize;
    private int rightSize;
    private DoubleLinkedList<CurrentContent> redoList;
    private TypeTreeWrapper typeWrapper;
    private JTree tree;
    private JButton btnRctVst;
    private JPopupMenu popRecent;
    private Stack<Note> stkRecent;
    public static final String KEY_EXPORT_PATH="export.path";
    private static PropertiesConfiguration pc=PropHelper.getPropConfigInstance();
    private static final String RECENT_VIEWED_PATH=SysPropUtil.getTempDir()+ "\\recentNote.tmp";
    private final static Log log = LogFactory.getLog(FrmMainI.class);
    private ConfigParser configParser=ConfigParser.getInstance();
    /**
     * Creates new form FrmMain
     */
    public FrmMainI() {
        configParser.loadConfig();
        initComponents();
        initRecentViewed();
        addRecentVisitingButton();
        setTitle("NoteBook--MyE9");

        try {
            initBO();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            XdgUtil.showError(FrmMainI.this, e);
        }

        initTypeTree();

        initAction();

        // set menu bar
        setMenuBar();

        setToolbar();

        // set right menu
        setRightMenu();

        // setting
        setInitialState();

    }

    private void initRecentViewed() {
       stkRecent=(Stack<Note>)MiscUtil.deserialize(RECENT_VIEWED_PATH);
    }

    private void addRecentVisitingButton() {
        btnRctVst = new JButton("Recently Seen");

        if (stkRecent==null) {
            stkRecent = new Stack<Note>();
        }
//        stkRecent.setSize(15);
        btnRctVst.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                popRecent = new JPopupMenu();
                int i = 0;
//                 for (final Note n:stkRecent){
//                     System.out.println(n);
//                 }


                for (int k = stkRecent.size() - 1; k >= 0; k--) {
//                    final Note n=it.next();
                    final Note n = stkRecent.get(k);
                    ++i;
                    if (i > Constant.MAX_RECENT_BROWSED_REC) break;
                    popRecent.add(new AbstractAction("No." + i + " " + n.getTitle() + "  //" + n.getNoteType().getName()) {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (saveBeforeAction() == false) return;
                            } catch (NoSuchMethodException e1) {
                                e1.printStackTrace();
                            } catch (IllegalAccessException e1) {
                                e1.printStackTrace();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            } catch (InvocationTargetException e1) {
                                e1.printStackTrace();
                            }

                            selectNote(n.getId(), n.getNoteType().getId());
                        }
                    });
                }

                popRecent.show(pnlRightTop, btnRctVst.getX(),
                        btnRctVst.getY() + btnRctVst.getHeight());

            }
        });

        pnlRightTop.add(btnRctVst);
    }

    private void selectNote(int noteNo, int typeNo) {
        typeWrapper.selectNodeByType(typeNo);
        try {
            cbKey.getEditor().setItem("");
            search(false);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        for (int j = 0; j < tblData.getRowCount(); j++) {
            Note n2 = (Note) currentModel.get(j);
            if (noteNo == n2.getId()) {
                currentModel.selectRow(j);
                break;
            }

        }
    }

    private void initTypeTree() {
        typeWrapper = new TypeTreeWrapper(FrmMainI.this, bookBO);
        tree = typeWrapper.getTree();
        jspType.setViewportView(typeWrapper.getPanel());
    }

    private void initBO() throws IOException {

        noteDAO = new NoteDAOIbts("book.note", DBConnection.createSessionFactory());
        typeDAO = new NoteTypeDAOIbts("book.type", DBConnection.createSessionFactory());
        favDAO = new FavoriteDAOIbts("book.favorite", DBConnection.createSessionFactory());
        bookBO = new BookBO();
        bookBO.setNoteDAO(noteDAO);
        bookBO.setTypeDAO(typeDAO);
        bookBO.setFavDAO(favDAO);
    }

    private void setToolbar() {
        Action save = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                save();
            }
        };
        save.putValue(Action.SMALL_ICON, getIcon("save.png"));
        save.putValue(Action.SHORT_DESCRIPTION, "Save");

        Action exit = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                exit();
            }
        };
        exit.putValue(Action.SMALL_ICON, getIcon("close.png"));
        exit.putValue(Action.SHORT_DESCRIPTION, "Close");

        Action insert = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                insert();
            }
        };
        insert.putValue(Action.SMALL_ICON, getIcon("insert.png"));
        insert.putValue(Action.SHORT_DESCRIPTION, "Insert");

        Action delete = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                delete(false);
            }
        };
        delete.putValue(Action.SMALL_ICON, getIcon("delete.png"));
        delete.putValue(Action.SHORT_DESCRIPTION, "Delete");

        Action reopen = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                reopen();
            }
        };
        reopen.putValue(Action.SMALL_ICON, getIcon("reopen.png"));
        reopen.putValue(Action.SHORT_DESCRIPTION, "Reopen");

        JToolBar toolbar = new JToolBar();
        toolbar.add(save);
        toolbar.add(exit);
        toolbar.add(insert);
        toolbar.add(delete);
        toolbar.add(reopen);
        add(toolbar, BorderLayout.NORTH);

    }

    /**
     * close the window, exit app
     *
     * @return
     */
    private void exit()  {
        if (currentModel == null) {
            System.exit(0);
            return;
        }
        currentModel.stopEditing();
        try {
            if (saveBeforeAction() == false) {
                return;
            }
            currentModel.closeConnection();

            MiscUtil.serialize(stkRecent, RECENT_VIEWED_PATH);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace(); // To change body of catch statement use File
            // | Settings | File Templates.
            Msg.showError(this, e1);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace(); // To change body of catch statement use File
            // | Settings | File Templates.
            Msg.showError(this, e1);
        } catch (SQLException e1) {
            e1.printStackTrace(); // To change body of catch statement use File
            // | Settings | File Templates.
            Msg.showError(this, e1);
        } catch (InvocationTargetException e1) {
            e1.printStackTrace(); // To change body of catch statement use File
            // | Settings | File Templates.
            Msg.showError(this, e1);
        } catch (IOException e) {

        }
        System.exit(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        splAll = new JSplitPane();
        jspType = new JScrollPane();
        splRight = new JSplitPane();
        pnlRightUp = new JPanel();
        pnlRightTop = new JPanel();
        jLabel1 = new JLabel();
        lblCount = new JLabel();
        jLabel3 = new JLabel();
        cbKey = new JComboBox();
        btnSearch = new JButton();
        tfInnerSearch = new JTextField();
        lblMatched = new JLabel();
        jScrollPane2 = new JScrollPane();
        tblData = new JTable();
        jScrollPane3 = new JScrollPane();
        taContent = new JTextArea();
        pnlState = new JPanel();
        lblState = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        splAll.setDividerLocation(180);
        splAll.setDividerSize(10);
        splAll.setOneTouchExpandable(true);
        splAll.setLeftComponent(jspType);

        splRight.setDividerLocation(300);
        splRight.setDividerSize(10);
        splRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splRight.setOneTouchExpandable(true);

        pnlRightUp.setPreferredSize(new Dimension(931, 30));
        pnlRightUp.setLayout(new BorderLayout());

        pnlRightTop.setLayout(new FlowLayout(FlowLayout.LEFT));

        jLabel1.setText("there are ");
        pnlRightTop.add(jLabel1);

        lblCount.setForeground(new Color(0, 51, 255));
        lblCount.setText("jLabel2");
        pnlRightTop.add(lblCount);

        jLabel3.setText(" records in all");
        pnlRightTop.add(jLabel3);

        cbKey.setEditable(true);
        cbKey.setPreferredSize(new Dimension(200, 21));
        pnlRightTop.add(cbKey);

        btnSearch.setText("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        pnlRightTop.add(btnSearch);

        tfInnerSearch.setPreferredSize(new Dimension(100, 21));
        tfInnerSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                tfInnerSearchKeyReleased(evt);
            }
        });
        pnlRightTop.add(tfInnerSearch);
        pnlRightTop.add(lblMatched);

        pnlRightUp.add(pnlRightTop, BorderLayout.NORTH);

        jScrollPane2.setEnabled(false);
        jScrollPane2.setViewportView(tblData);

        pnlRightUp.add(jScrollPane2, BorderLayout.CENTER);

        splRight.setTopComponent(pnlRightUp);

        taContent.setColumns(20);
        taContent.setLineWrap(true);
        taContent.setRows(5);
        taContent.setTabSize(2);
        jScrollPane3.setViewportView(taContent);

        splRight.setRightComponent(jScrollPane3);

        splAll.setRightComponent(splRight);

        getContentPane().add(splAll, BorderLayout.CENTER);

        pnlState.setLayout(new BorderLayout());

        lblState.setHorizontalAlignment(SwingConstants.LEFT);
        pnlState.add(lblState, BorderLayout.CENTER);

        getContentPane().add(pnlState, BorderLayout.SOUTH);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 933) / 2, (screenSize.height - 734) / 2, 933, 734);
    }// </editor-fold>

    private void tfInnerSearchKeyReleased(KeyEvent evt) {
        searchInContent();
    }

    private void searchInContent() {
        taContent.getHighlighter().removeAllHighlights();
        String key = tfInnerSearch.getText();
        String content = taContent.getText();
        if (XdgUtil.isEmpty(content)) {
            return;
        }
        if (XdgUtil.isEmpty(tfInnerSearch.getText())) {
            return;
        }


        String[] splited = content.split("(?i)" + key);
        lblMatched.setText((splited.length == 1 ? 0 : splited.length - 1) + " matched");
        Pattern ptn = Pattern.compile(key, Pattern.CASE_INSENSITIVE + Pattern.MULTILINE + Pattern.UNICODE_CASE);
        matcher = ptn.matcher(content);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            try {
                taContent.getHighlighter().addHighlight(start, end, new DefaultHighlighter.DefaultHighlightPainter(Color.yellow));
            } catch (BadLocationException ex) {
                Logger.getLogger(FrmMainI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void btnSearchActionPerformed(ActionEvent evt) {// GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_btnSearchActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        createWin();
    }

    public static void createWin() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(new Runnable() {

            public void run() {
                JFrame frame = new FrmMainI();
//                frame.setSize(980,600);
                frame.setSize(950, frame.getHeight()); //previously 933
                frame.setVisible(true);
//                System.out.println(frame.getWidth()+"");
            }
        });
    }

    private void setMenuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu common = new JMenu("Common Operation");
        JMenuItem itReopen = new JMenuItem("Reenter");
        JMenuItem itImport = new JMenuItem("Import");
        JMenuItem itExport = new JMenuItem("Export");
        JMenuItem itClose = new JMenuItem("Exit");
        JMenuItem itPhysicalDel=new JMenuItem("Physical Deletion");
        common.add(itImport);
        common.add(itExport);
        common.add(itPhysicalDel);
        common.add(itReopen);
        common.add(itClose);

        JMenu maintain = new JMenu("Maintain");
        JMenuItem itChangeType = new JMenuItem("Change NoteType");
        JMenuItem itHide = new JMenuItem("Hide Current NoteType");
        maintain.add(itChangeType);
        maintain.add(itHide);

        // set item action
        itReopen.addActionListener(actOpen);
        itClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                thisWindowClosing();
            }
        });

        itImport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FrmImport frmImport=new FrmImport(bookBO);
                frmImport.pack();
                frmImport.setVisible(true);
                XdgUtil.displayInMiddle(frmImport);
            }
        });

        itExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportAction();
            }

        });

        itChangeType.addActionListener(actTypeMaintain);
        itHide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tree.getSelectionCount() != 0) {
                    hideOneType();
                }
            }
        });

        itPhysicalDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delete(true);
            }
        });

        final JMenu favorite = new JMenu("Favorite Folder");
        favorite.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {

                List<FavoriteNote> favoriteItems = bookBO.getFavoriteItemList();
                favorite.removeAll();

                favorite.add(new AbstractAction("Manage Favorite") {
                    public void actionPerformed(ActionEvent e) {
                        new FrmFavorite(bookBO);
                    }
                });

                favorite.addSeparator();
                for (final FavoriteNote item : favoriteItems) {
                    favorite.add(new JMenuItem(new AbstractAction(item.getNote().getTitle() + "  //" + item.getNoteType().getName()) {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (saveBeforeAction() == false) return;
                            } catch (NoSuchMethodException e1) {
                                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (IllegalAccessException e1) {
                                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (SQLException e1) {
                                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (InvocationTargetException e1) {
                                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }

                            selectNote(item.getNote().getId(), item.getNoteType().getId());
                        }
                    }));
                }
            }

            public void menuDeselected(MenuEvent e) {

            }

            public void menuCanceled(MenuEvent e) {

            }
        });


        mb.add(common);
        mb.add(maintain);
        mb.add(favorite);
        setJMenuBar(mb);

    }
    
    private void exportAction() {
		if (tblData.getSelectedRows().length == 0) return;
        String path = pc.getString(KEY_EXPORT_PATH, MyUtil.getDesktopPath());
        JFileChooser chooser = new JFileChooser(MyUtil.getDesktopPath());
        int rst=chooser.showSaveDialog(FrmMainI.this);
        if (rst!=JFileChooser.APPROVE_OPTION){
            return;
        }

        File file = chooser.getSelectedFile();
        if (file != null) {
            try {
                exportRecords(file);
            } catch (IOException e1) {
               XdgUtil.showError(null,e1);
                e1.printStackTrace();
            }
        }
	}

    private void exportRecords(File file) throws IOException {
        System.out.println("exporting..........");
                int[] rows = tblData.getSelectedRows();
                ArrayList<Note> export = new ArrayList<Note>();
                for (int i = 0; i < rows.length; i++) {
                    int row = tblData.convertRowIndexToModel(rows[i]);
                    Note n = (Note) currentModel.get(row);
                    String typeName=currentModel.getValueAt(row, 3).toString();
                    n.setTypeName(typeName);
                    export.add(n);
                }
        MyUtil.serialize(file.getPath(),export);
    }

    private void initAction() {
        actOpen = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                reopen();
            }
        };

        actTypeMaintain = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tblData.getSelectedRowCount() == 0) {
                    XdgUtil.showMsg(FrmMainI.this, "You haven't selected notes");
                    return;
                }

                TypeCallback callback=new TypeCallback() {
                    public void handle(NoteType type) {
                        if (XdgUtil.showConfirm(null, "You sure to change the test to '"
                                + type.getName() + "'?") != 1) return;

                        ArrayList<Note> notes = new ArrayList<Note>();
                        int[] selected = tblData.getSelectedRows();
                        for (int i = 0; i < selected.length; i++) {
                            Note note = (Note) currentModel.get(tblData.convertRowIndexToModel(selected[i]));

                            bookBO.updateNoteType(note.getId(), type.getId());
                        }
                    }
                };



                DlgType dlgType = new DlgType(FrmMainI.this,true,bookBO,callback);
                dlgType.setSize(400, 600);
                dlgType.setVisible(true);
                XdgUtil.displayInMiddle(dlgType);

            }
        };
    }

    private void openTypeMaintain() {
//        FrmTypes frmTypes = new FrmTypes();
//        frmTypes.setSize(500, 600);
//        frmTypes.setVisible(true);
//        XdgUtil.displayInMiddle(frmTypes);
    }

    private void setRightMenu() {
        popTypeTree = new JPopupMenu();
        popTable = new JPopupMenu();
        // JMenuItem itHide=new JMenuItem("闅愯棌");
        // JMenuItem itDelete=new JMenuItem("鍒犻櫎");

        popTypeTree.add(new AbstractAction("Hide") {

            public void actionPerformed(ActionEvent e) {
                hideOneType();
            }
        });

        popTypeTree.add(new AbstractAction("Delete") {
            public void actionPerformed(ActionEvent e) {
                if (typeWrapper.isNotSelecting()) return;
                NoteType noteType = (NoteType) typeWrapper.getSelectedType();
                if (Msg.showConfirm(FrmMainI.this, "Really delete \"" + noteType + "\"?") == 1) {
                    try {
                        typeDAO.deepDelete(noteType.getId());
                        typeWrapper.removeSelectedNode();
                    } catch (Exception ex) {
                        Msg.showError(FrmMainI.this, ex);
                    }
                }
            }
        });

        popTypeTree.add(new AbstractAction("NoteType Maintainence") {
            public void actionPerformed(ActionEvent e) {
                openTypeMaintain();
            }
        });

        itemHiddenTypes = new JMenu("Hidden Types");
        popTypeTree.add(itemHiddenTypes);

        //pop menu for jtable
        popTable.add(new AbstractAction("Add to favorite folder") {
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = tblData.getSelectedRows();
                if (selectedRows.length == 0) return;
                for (int i = 0; i < selectedRows.length; i++) {
                    Note n = (Note) currentModel.get(tblData.convertRowIndexToModel(selectedRows[i]));
                    bookBO.addFavoriteItem(n.getId());
                }
            }
        });
        
        popTable.add(new AbstractAction(("Export")) {
			public void actionPerformed(ActionEvent e) {
				exportAction();
			}
		});
        
        popTable.add(new AbstractAction("Highlight the related type") {
            public void actionPerformed(ActionEvent e) {
               Note selected=(Note)currentModel.get();
               typeWrapper.selectNodeByType(selected.getTypeId());
            }
        });
      

    }

    /**
     * 闅愯棌涓�釜绫诲埆
     */
    private void hideOneType() {
        currentModel.stopEditing();
        try {
            if (saveBeforeAction()) {
                if (Msg.showConfirm(FrmMainI.this, "Really hide it ?") == 1) {
                    NoteType noteType = (NoteType) typeWrapper.getSelectedType();
                    bookBO.hideType(noteType.getId());
                    typeWrapper.removeSelectedNode();
                }
            }
        } catch (Exception ex) {
            Msg.showError(FrmMainI.this, ex);
        }
    }

    private void setInitialState() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        commonSearch = new CommonAction();
        btnSearch.addActionListener(commonSearch);
        tblData.setRowHeight(30);
        Font font = new Font("Dialog", 0, 16);
        tblData.setFont(font);
        taContent.setFont(font);
        cbKey.setFont(font);
        cbKey.addItem("");
        cbKey.setSelectedIndex(0);
        taContent.setDragEnabled(true);


        setTitle("Notebook by Kurt");
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                FrmMainI.class.getResource("/images/Floppy.jpg")));

        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                thisWindowClosing();
            }
        });

        concreteShortcut("ctrl K", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(taContent.getText());// 灏嗗瓧绗︿覆鍖呰
                clip.setContents(selection, null);
            }
        });

        XdgUtil.addShortcut(taContent, "ctrl Z", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                previousEdit();
            }
        });

        XdgUtil.addShortcut(taContent, "ctrl Y", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                nextEdit();
            }
        });

        cbKey.getEditor().addActionListener(commonSearch);
        cbKey.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                //search in all types , including title and content
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown() && e.isControlDown()) {
                    try {
                        if (saveBeforeAction() == false) return;
                        tree.clearSelection();
                        tfInnerSearch.setText(MyUtil.getComboBoxValue(cbKey));
                        search(true);
//                        cbKey.getEditor().
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Msg.showError(FrmMainI.this, e1);
                    }
                } //search in all types, just from title
                else if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    try {
                        if (saveBeforeAction() == false) return;
                        tree.clearSelection();
                        search(false);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Msg.showError(FrmMainI.this, e1);
                    }
                }//search in current test,from both title and content
                else if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown()) {
                    try {
                        if (saveBeforeAction() == false) return;
                        tfInnerSearch.setText(MyUtil.getComboBoxValue(cbKey));
                        search(true);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Msg.showError(FrmMainI.this, e1);
                    }
                }


            }
        });

        MyUtil.enableTabIndent(taContent);
        MyUtil.enableInsertTabOnNewLine(taContent);

        try {
            backupDB();

            initData();

        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
            Msg.showMsg(this, e.getMessage());
        }

        setShortcut();
        setListenerToTypes();
        setListenerToContent();

        painting = false;
        tree.requestFocus();
        tree.requestFocusInWindow();
//
//		TableRowSorter<NoteTableModel> sorter=
//			new TableRowSorter<NoteTableModel>((NoteTableModel)tblData.getModel());
//
//		tblData.setRowSorter(sorter);

        tblData.setAutoCreateRowSorter(true);

        //jtable right menu
        tblData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popTable.show(tblData, e.getX(), e.getY());
                }
            }
        });
    }



    private void thisWindowClosing() {
        exit();
    }

    private void initData() throws Exception, SQLException,
            ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {


        Properties prop = new Properties();
        prop.load(FrmMainI.class.getResourceAsStream("/config.properties"));
        Reader reader = Resources.getResourceAsReader("dao/ibatis/dao.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader, prop);

        BookBO bo = new BookBO();


        // typeDAO=new NoteTypeDAOIbts();
        // noteDAO=new NoteDAOIbts();
//        List types = typeDAO.findAllWithRecSum(
//                Constant.TYPE_SORT_RECORD_ALPHA, Constant.SORT_ASC);
//        Collections.sort(types);
//        listTypes = typeDAO.findAllWithoutRecSum(Constant.TYPE_SORT_DESC,
//                Constant.SORT_ASC);
//        typeEditor = new TypeEditor(listTypes, "id");
//        typeRender = new TypeRender(listTypes, "id", "name");
//
//        typeModel = new ExtDefaultListModel(types);

        // setListenerToTypes();
//        search();
        taContent.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                isInFocus = true;
                MyUtil.setChineseInput(taContent);
            }

            public void focusLost(FocusEvent e) {
                isInFocus = false;
            }
        });
        taContent.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                // if (isInFocus == false) {
                // return;
                // }
                // if (currentModel.getState() == dbwin.Constant.NOT_CHANGED) {
                // currentModel.setState(dbwin.Constant.MODIFIED);
                // ((Note)
                // currentModel.getMappedType()).setContent(taContent.getText());
                // System.out.println("doc:" + taContent.getText());
                // }
            }

            public void insertUpdate(DocumentEvent e) {
                if (isInFocus == false) {
                    return;
                }

                ((Note) currentModel.get()).setContent(taContent.getText().trim().length() == 0 ? null : taContent.getText().trim());
                if (currentModel.getStatus() == dbwin.Constant.NOT_CHANGED) {
                    currentModel.setStatus(dbwin.Constant.MODIFIED);
                }
            }

            public void removeUpdate(DocumentEvent e) {
                if (isInFocus == false) {
                    return;
                }

                ((Note) currentModel.get()).setContent(taContent.getText().trim().length() == 0 ? null : taContent.getText().trim());
                if (currentModel.getStatus() == dbwin.Constant.NOT_CHANGED) {
                    currentModel.setStatus(dbwin.Constant.MODIFIED);
                }
            }
        });

    }

    public void search(boolean searchContent) throws Exception, ClassNotFoundException,
            IllegalAccessException, NoSuchMethodException,
            InvocationTargetException {
//        if (saveBeforeAction()==false){
//              return;
//        }
        saveKey();

        int firstTypeNo = 0;
//        if (lstType.getVisibleRowCount() > 0 && lstType.getSelectedIndex() >= 0) {
//            firstTypeNo = ((NoteType) lstType.getSelectedValue()).getId();
//        }

        searchedNotes = null;

        try {
            if (searchContent) {
                searchedNotes = bookBO.findNoteByTypeTitleContent(typeWrapper.isNotSelecting() ? null : typeWrapper.getSelectedNode(),
                        MyUtil.getComboBoxValue(cbKey));
            } else {
                searchedNotes = bookBO.findNoteByTypeTitle(typeWrapper.isNotSelecting() ? null : typeWrapper.getSelectedNode(),
                        MyUtil.getComboBoxValue(cbKey));
            }
        } catch (Exception e) {
            log.error(e);
            throw e;
        }


        taContent.setText("");
        lblCount.setText(searchedNotes.size() + "");
        currentModel = new NoteTableModel2(DBConnection.getConnection(configParser.getDriver(),
                configParser.getRealUrl(),configParser.getUserName(),configParser.getPassword()),
                tblData, searchedNotes, "ID", "id", "book",
                dbwin.Constant.KEY_GENE_NEXT_OF_MAX, this);
        currentModel.setFieldPropCol(new String[]{"TITLE", "TIM", "TYPE_NO",
                "ID", "CONTENT", "DEL"}, new String[]{"title", "ts", "typeId",
                "id", "content", "del"}, new String[]{"Title", "Creation Date", "NoteType", "", "", ""});
        currentModel.setShownTitleNames(new String[]{"No.", "Title", "Creation Date",
                "NoteType"});
        currentModel.setEditableCols(new int[]{1, 2});
        tblData.setModel(currentModel);
        setListenerToNotes();

        //set test text align
        DefaultTableCellRenderer typeRender = new DefaultTableCellRenderer();
        typeRender.setHorizontalAlignment(SwingConstants.LEFT);
        tblData.getColumnModel().getColumn(3).setCellRenderer(typeRender);

        currentModel.setEditorFont();
        currentModel.setColWidth(0, 52);
        currentModel.setColWidth(2, 170);
        currentModel.setColWidth(3, 120);
//        currentModel.setRender(3, typeRender);
//        currentModel.setEditor(3, typeEditor);
        currentModel.stopEditing();
        currentModel.trigEditClose();


    }

    public void setListenerToTypes() {
//        tree.addTreeSelectionListener(new TreeSelectionListener() {
//            public void valueChanged(TreeSelectionEvent e) {
//                try {
//
//                    if (tree.isNotSelecting() ||
//                            tree.getSelectedType() == null ||
//                            tree.getSelectedType().getId() == null) return;
//                    cbKey.getEditor().setItem("");
//                    search(false);
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                    XdgUtil.showError(FrmMainI.this, e1);
//                }
//            }
//        });

        VetoTreeSelectionModel selectionModel = new VetoTreeSelectionModel();
        selectionModel.addTreeSelectionListener(new VetoTreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                try {
                    if (typeWrapper.isNotSelecting() ||
                            typeWrapper.getSelectedType() == null ||
                            typeWrapper.getSelectedType().getId() == null) return;
                    cbKey.getEditor().setItem("");
                    search(false);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    XdgUtil.showError(FrmMainI.this, e1);
                }
            }

            public boolean valueChanging(TreeSelectionEvent event) {
                try {
                    if (currentModel != null) currentModel.stopEditing();
                    return saveBeforeAction();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InvocationTargetException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                return false;
            }
        });

        tree.setSelectionModel(selectionModel);
    }

    public void setListenerToNotes() {
        if (searchedNotes.size() == 0) {
            return;
        }
        tblData.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            if (tblData.getSelectedRow() == -1) {
                                return;
                            }

                            redoList = new DoubleLinkedList<CurrentContent>();

                            // System.out.println("鎹㈣浜嗐�firstIndex="+e.getFirstIndex()+",lastIndex="+e.getLastIndex());
                            int realRow = tblData.convertRowIndexToModel(tblData.getSelectedRow());
                            Note n = (Note) searchedNotes.get(realRow);
                            taContent.setText(n.getContent());
                            lblState.setText(String.format("  ID: %d, Title: %s, NoteType ID: %d", n.getId(), n.getTitle(), n.getTypeId()));

                            // scrlContent.getViewport().setViewPosition(new
                            // Point(0,0));
                            taContent.setCaretPosition(0);
                            if (XdgUtil.isNotEmptyOrNull(tfInnerSearch.getText())) {
                                searchInContent();
                            }

                            //save selection situation
                            if (stkRecent.isEmpty()) {
                                stkRecent.push(n);
                            } else if (stkRecent.peek().getId() != n.getId()) {
                                stkRecent.push(n);
                            }
                            // scrlContent.getHorizontalScrollBar().setValue(0);
                            // tblData.editCellAt(tblData.getSelectedRow(),1);
                            // currentModel.getCursor(1);
                        }
                    }
                });
    }

    public void setListenerToContent() {
        taContent.getDocument().addDocumentListener(new DocumentListener() {

            public void removeUpdate(DocumentEvent e) {
                record();

            }

            public void insertUpdate(DocumentEvent e) {
                // TODO Auto-generated method stub
                record();
            }

            public void changedUpdate(DocumentEvent e) {
                // TODO Auto-generated method stub
//				record();
            }

            private void record() {
//				System.out.println("current pos:"+taContent.getCaretPosition());
                redoList.add(new CurrentContent(taContent.getText(), taContent.getCaretPosition()));
            }
        });
    }

    public void previousEdit() {
        if (redoList.hasPrevious()) {
            CurrentContent cc = redoList.previous();
            taContent.setText(cc.getContent());
            taContent.setCaretPosition(cc.getPos());
        }
    }

    public void nextEdit() {
        if (redoList.hasNext()) {
            CurrentContent cc = redoList.next();
            taContent.setText(cc.getContent());
            taContent.setCaretPosition(cc.getPos());
        }
    }

    public void setShortcut() {
        JComponent[] manuals = {tree, tblData, taContent};
        // concreteShortcut(manuals,"ctrl A",new AbstractAction(){
        // public void actionPerformed(ActionEvent e) {
        // tfKey.setText("");
        // tfKey.postActionEvent();
        // }
        // });

        // 鎻掑叆
        concreteShortcut("ctrl N", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                insert();
            }
        });
        // 鍒犻櫎
        concreteShortcut("ctrl D", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                delete(false);
            }
        });
        // 淇濆瓨
        concreteShortcut("ctrl S", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        XdgUtil.addShortcut(taContent, "ctrl E", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                XdgUtil.deleteCurrentRow(taContent);
            }
        });

        XdgUtil.addShortcut(taContent, "ctrl alt M", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                if (isMin) {
                    splAll.setDividerLocation(leftSize);
                    splRight.setDividerLocation(rightSize);
                    isMin = false;
                } else {
                    leftSize = splAll.getDividerLocation();
                    rightSize = splRight.getDividerLocation();
                    isMin = true;
                    splAll.setDividerLocation(0);
                    splRight.setDividerLocation(0);
                }
            }
        });
    }

    public void concreteShortcut(String key, Action action) {
        // JComponent rp= (JComponent)this.getGlassPane();
        JRootPane rp = this.getRootPane();
        InputMap imap = rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap amap = rp.getActionMap();
        String mapKey = "sc-" + new Random().nextInt();
        imap.put(KeyStroke.getKeyStroke(key), mapKey);
        amap.put(mapKey, action);
    }

    public void concreteShortcut(JComponent comp, KeyStroke ks, Action action) {
        String mapKey = new Random().nextInt() + "";
        InputMap imap = comp.getInputMap();
        ActionMap amap = comp.getActionMap();
        imap.put(ks, mapKey);
        amap.put(mapKey, action);
    }

    /**
     * 淇濆瓨
     */
    private void save() {
        try {
            currentModel.stopEditing();
            currentModel.save();
        } catch (SQLException e1) {
            e1.printStackTrace(); // To change body of catch statement use File
            // | Settings | File Templates.
            Msg.showError(FrmMainI.this, e1);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace(); // To change body of catch statement use File
            // | Settings | File Templates.
            Msg.showError(FrmMainI.this, e1);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace(); // To change body of catch statement use File
            // | Settings | File Templates.
            Msg.showError(FrmMainI.this, e1);
        } catch (InvocationTargetException e1) {
            e1.printStackTrace(); // To change body of catch statement use File
            // | Settings | File Templates.
            Msg.showError(FrmMainI.this, e1);
        }
    }

    private void reopen() {
        try {
            if (saveBeforeAction() == false) {
                return;
            }
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace(); // To change body of catch statement use File
            // | Settings | File Templates.
            Msg.showError(FrmMainI.this, e1);
            return;
        } catch (IllegalAccessException e1) {
            e1.printStackTrace(); // To change body of catch statement use File
            // | Settings | File Templates.
            Msg.showError(FrmMainI.this, e1);
            return;
        } catch (SQLException e1) {
            e1.printStackTrace(); // To change body of catch statement use File
            // | Settings | File Templates.
            Msg.showError(FrmMainI.this, e1);
            return;
        } catch (InvocationTargetException e1) {
            e1.printStackTrace(); // To change body of catch statement use File
            // | Settings | File Templates.
            Msg.showError(FrmMainI.this, e1);
            return;
        }
        FrmMainI.this.setVisible(false);
        createWin();
        FrmMainI.this.dispose();
    }

    private void insert() {
        if (typeWrapper.isNotSelecting()) {
            Msg.showMsg(FrmMainI.this, "Can't insert when no test selected");
            return;
        }

        NoteType noteType = typeWrapper.getSelectedType();
        Note note = new Note();
        note.setTs(new Timestamp(System.currentTimeMillis()));
        note.setNoteType(noteType);
        note.setTypeId(noteType.getId());
        note.setDel(0);
        FrmMainI.this.currentModel.insert(note);
        int row = searchedNotes.size() - 1;
    }

    private void delete(boolean physicalDel) {
        if (JOptionPane.showConfirmDialog(FrmMainI.this, "Do you really want to delete?", "Prompt",
                JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                if (physicalDel){
                    FrmMainI.this.currentModel.delete();
                } else {
                    FrmMainI.this.currentModel.logicalDelete("del", 1);
                }
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace(); // To change body of catch statement use
                // File | Settings | File Templates.
                Msg.showError(FrmMainI.this, e1);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace(); // To change body of catch statement use
                // File | Settings | File Templates.
                Msg.showError(FrmMainI.this, e1);
            } catch (InvocationTargetException e1) {
                e1.printStackTrace(); // To change body of catch statement use
                // File | Settings | File Templates.
                Msg.showError(FrmMainI.this, e1);
            } catch (SQLException e1) {
                e1.printStackTrace(); // To change body of catch statement use
                // File | Settings | File Templates.
                Msg.showError(FrmMainI.this, e1);
            }
        }
    }

    /**
     * 鍏抽棴銆佹敼鍙樼被鍒�閲嶆柊鎼滅储绛夋搷浣滃墠鍒ゆ柇鏁版嵁鏄惁鏀瑰彉锛屽苟淇濆瓨
     *
     * @return true:鍏佽杩涜涓嬩竴姝ワ紝false;涓嶅厑璁�
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws java.sql.SQLException
     * @throws java.lang.reflect.InvocationTargetException
     *
     */
    public boolean saveBeforeAction() throws NoSuchMethodException,
            IllegalAccessException, SQLException, InvocationTargetException {
        if (currentModel == null || currentModel.getData().size() == 0) {
            return true;
        }

        if (currentModel.isChanged()) {
            int action = JOptionPane.showConfirmDialog(this, "Data changed, save?",
                    "Prompt", JOptionPane.YES_NO_CANCEL_OPTION);
            if (action == JOptionPane.YES_OPTION) {
                currentModel.save();
                return true;
            } else if (action == JOptionPane.NO_OPTION) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void saveKey() {
        String key = MyUtil.getComboBoxValue(cbKey);
        if (MyUtil.isEmpty(key)) {
            return;
        } else {

            if (cbKey.getItemCount() == 1) {
                cbKey.addItem(key);
            } else if (cbKey.getItemCount() == 2) {
                if (!cbKey.getItemAt(1).equals(key)) {
                    cbKey.addItem(key);
                }
            } else {
                // tfKey.getItemCount();
                // tfKey.removeItemAt(tfKey.getItemCount()-1);

                for (int i = cbKey.getItemCount() - 1; i >= 0; i--) {
                    if (cbKey.getItemAt(i).equals(key)) {
                        cbKey.removeItemAt(i);
                    }
                }

                cbKey.insertItemAt(key, 1);
            }

            cbKey.getEditor().setItem(key);

        }

    }

    private static Image getImage(String name) {
        final Image image = Toolkit.getDefaultToolkit().getImage(
                FrmMainI.class.getResource("/images/" + name));
        return image;
    }

    private static ImageIcon getIcon(String name) {
        return new ImageIcon(getImage(name));
    }

    private void backupDB() throws IOException {
        //new start
        //todo back up db through local net, which is from lapto to home pc
        if (configParser.getCurrentPlace().contains("home") && configParser.getCurrentPlace().contains("pc")){
            return;
        }

        PropUtil pu=PropUtil.getInstance(Constant.CONFIG_PROP_PATH);
        long lastBackupMs=pu.getLong("last.backup.ms",0);
        if (lastBackupMs==0){
            pu.addAndSave("last.backup.ms",new Date().getTime()+"");
            pu.addAndSave("last.backup.string",DateTimeUtil.formatYmdHms(new Date()));
        }else {
           Date lastBackupDate= new Date(lastBackupMs);
           double dayIntvl= DateTimeUtil.diffByDay(new Date(lastBackupMs),new Date());
           if (dayIntvl>configParser.getBackupIntvl()){

           }
        }
    }

    private class CommonAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            beforeSearch();
        }

        public void beforeSearch() {

            if (painting) {
                return;
            }

            if (typeWrapper.isNotSelecting()) {
                Msg.showMsg(FrmMainI.this, "No test selected");
            }

            try {
                if (saveBeforeAction() == true) {
                    search(false);
                }
            } catch (Exception e1) {
                e1.printStackTrace(); // To change body of catch statement use
                // File | Settings | File Templates.
                Msg.showError(FrmMainI.this, e1);
            }
        }
    }


    // Variables declaration - do not modify
    private JButton btnSearch;
    private JComboBox cbKey;
    private JLabel jLabel1;
    private JLabel jLabel3;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JScrollPane jspType;
    private JLabel lblCount;
    private JLabel lblMatched;
    private JLabel lblState;
    private JPanel pnlRightTop;
    private JPanel pnlRightUp;
    private JPanel pnlState;
    private JSplitPane splAll;
    private JSplitPane splRight;
    private JTextArea taContent;
    private JTable tblData;
    private JTextField tfInnerSearch;
    // End of variables declaration
}

class NoteTableModel2 extends DbWinModel {

    public NoteTableModel2(Connection cnn, JTable table, List data,
                           String keyField, String keyProperty, String tableName,
                           int keyGeneWay, JFrame frame) {
        super(cnn, table, data, keyField, keyProperty, tableName, keyGeneWay,
                frame);

    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Object getValueAt(int row, int col) {
//		rowIndex=getView().convertRowIndexToModel(rowIndex);
//		columnIndex=getView().convertColumnIndexToModel(columnIndex);


        Note note = (Note) data.get(row);

//        if (note.getNoteType().getShow() == 0) {
//            setRenderBackground(row, col, Color.LIGHT_GRAY);
//        } else {
//            setRenderBackground(row, col, Color.white);
//        }

        if (col == 0) {
            return new Integer(row + 1);
        }

        if (col == 1) {
            return note.getTitle();
        } else if (col == 2) {
            return note.getTs();
        } else {
            return note.getNoteType().getName();
        }
    }

    @Override
    public Class getColumnClass(int c) {
        Class[] cls = {Integer.class, String.class, Timestamp.class,
                Integer.class};
        return cls[c];
    }
}


