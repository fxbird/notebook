package dbwin;

import com.xdg.util.hibernate.HbnExeUtil;
import com.xdg.util.hibernate.UpdateLogic;
import dbwin.render.TypeRender;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import other.Msg;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.List;

public abstract class HbnDbWinModel extends AbstractTableModel {
    protected List data;
    private String[] shownTitleNames;
    protected JTable view;
    private int[] editableCols;
    private JFrame frame;
    private Session session;
    private final static Log log = LogFactory.getLog(HbnDbWinModel.class);
    private String delCol="del";
    private String delField="del";
    private int deletedVal=1;
    private Map<String,String> colFieldMap=new HashMap<String, String>();

    public HbnDbWinModel(Session session, JTable table, List data, JFrame frame) {
        this.session = session;
        this.data = data;
        this.view = table;
        this.frame = frame;

        concreteShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                stopEditing();
            }
        });

    }

    public void setEditorFont() {
        for (int i = 0; i < getColumnCount(); i++) {
            if (!isCellEditable(0, i)) {
                continue;
            }
            CellEditor edit = view.getCellEditor(0, i);
            if (edit instanceof DefaultCellEditor) {
                ((DefaultCellEditor) edit).getComponent().setFont(view.getFont());
            }
        }
    }

    public void setRenderBackground(int r, int c, Color color) {
        TableCellRenderer render = view.getCellRenderer(r, c);
        if (render instanceof DefaultTableCellRenderer) {
            ((DefaultTableCellRenderer) render).setBackground(color);
        } else if (render instanceof TypeRender) {
            ((TypeRender) render).getComponent().setBackground(color);
        }

    }


    public void concreteShortcut(String key, Action action) {
//       JComponent rp= (JComponent)this.getGlassPane();
        JRootPane rp = frame.getRootPane();
        InputMap imap = rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap amap = rp.getActionMap();
        String mapKey = "sc-" + new Random().nextInt();
        System.out.println(mapKey);
        imap.put(KeyStroke.getKeyStroke(key), mapKey);
        amap.put(mapKey, action);
    }

    public void concreteShortcut(KeyStroke ks, Action action) {
        JRootPane rp = frame.getRootPane();
        String mapKey = new Random().nextInt() + "";
        InputMap imap = rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap amap = rp.getActionMap();
        imap.put(ks, mapKey);
        amap.put(mapKey, action);
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public String[] getShownTitleNames() {
        return shownTitleNames;
    }


    /**
     * 必须调用
     *
     * @param shownTitleNames
     */
    public void setShownTitleNames(String[] shownTitleNames) {
        this.shownTitleNames = shownTitleNames;
    }


    public int[] getEditableCols() {
        return editableCols;
    }

    /**
     * 必须调用
     *
     * @param editableCols
     */
    public void setEditableCols(int... editableCols) {
        this.editableCols = editableCols;
    }

    public void trigEditClose() {
//        if (data.size()==0){
//            return;
//        }
        for (int i = 0; i < getColumnCount(); i++) {
            if (!isCellEditable(0, i)) {
                continue;
            }
            CellEditor edit = view.getCellEditor(0, i);
            if (edit instanceof DefaultCellEditor) {
                ((DefaultCellEditor) edit).getComponent().addFocusListener(new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        stopEditing();
                    }
                });
            }

        }
    }

    /**
     * 结束单元格的编辑,如果只编辑一个单元格然后直接窗口是不会触发模型的setValueAt的
     */
    public void stopEditing() {
//        if (data.size()>0){
        if (view.isEditing()) {
            view.getCellEditor().stopCellEditing();
        }
//         }
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        if (shownTitleNames == null || shownTitleNames.length == 0) {
            Msg.showMsg(null, "No display column names array assigned!");
            return -1;
        } else {
            return shownTitleNames.length;
        }
    }

    public abstract Object getValueAt(int rowIndex, int columnIndex);

    public String getColumnName(int column) {
        if (shownTitleNames == null || shownTitleNames.length == 0) {
            Msg.showMsg(null, "No display column names array assigned!");
            return "";
        } else {
            return shownTitleNames[column];
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (editableCols == null) {
            Msg.showMsg(null, "No editable column index array for display assigned!");// 没有指定显示的可编辑的列索引数组
            return false;
        } else if (editableCols.length == 0) {
            return false;
        } else {
            return Arrays.binarySearch(editableCols, columnIndex) > -1;
        }
    }

    public void save() throws SQLException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (isChanged() == false) {
            return;
        }

        for (int i = 0; i < data.size(); i++) {
            final HbnWinBean wb = (HbnWinBean) data.get(i);
            if (wb.getState() == BeanState.Modified) {
                HbnExeUtil.executeUpdateLogic(session, new UpdateLogic() {
                    public void execute(Session session) {
                        session.update(wb);
                    }
                });
                wb.setState(BeanState.No_Change);
            } else if (wb.getState() == BeanState.New) {
                wb.setState(BeanState.New);
                HbnExeUtil.executeUpdateLogic(session, new UpdateLogic() {
                    public void execute(Session session) {
                        session.save(wb);
                    }
                });
                wb.setState(BeanState.No_Change);
            }
        }
    }

    public void delete() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SQLException {

        int[] rows = view.getSelectedRows();
        if (rows.length == 0) {
            return;
        }

        if (view.isEditing()) {
            view.getCellEditor().stopCellEditing();
        }

        for (int i = rows.length - 1; i >= 0; i--) {
            int rRow = view.convertRowIndexToModel(rows[i]);
            HbnWinBean wb = (HbnWinBean) data.get(rRow);
            if (wb.getState() != BeanState.New) {
                session.delete(wb);
            }

            data.remove(rRow);
            fireTableRowsDeleted(0, rRow);

        }
    }

    /**
     * logical delete, just set a mark column to a special value referring to logical delete
     *
     * @param field mark column name
     * @return updated row amount
     * @throws NumberFormatException
     * @throws IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     *
     * @throws NoSuchMethodException
     * @throws java.sql.SQLException
     */
    public int logicalDelete() throws NumberFormatException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, SQLException {
        int[] rows = view.getSelectedRows();
        if (rows.length == 0) {
            return 0;
        }

        int sum = 0;
        for (int i = rows.length - 1; i >= 0; i--) {
            int rRow = view.convertRowIndexToModel(rows[i]);
            final HbnWinBean wb = (HbnWinBean) data.get(rRow);
            if (wb.getState() != BeanState.New) {
                HbnExeUtil.executeUpdateLogic(session, new UpdateLogic() {
                    @Override
                    public void execute(Session session) throws InvocationTargetException, IllegalAccessException {
                        BeanUtils.setProperty(wb, delField, deletedVal);
                        session.update(wb);
                    }
                });
            }
            removeFromModel(rRow);
            sum++;
        }

        return sum;
    }

    public boolean isChanged() {
        for (int i = 0; i < data.size(); i++) {
            HbnWinBean elem = (HbnWinBean) data.get(i);
            if (elem.getState() == BeanState.Modified || elem.getState() == BeanState.New) {
                return true;
            }
        }

        return false;
    }

    public void setState(int row, BeanState state) {
        HbnWinBean wb = (HbnWinBean) data.get(row);
        wb.setState(state);
    }

    public void setState(BeanState state) {
        int row = view.getSelectedRow();
        if (row < 0) {
            return;
        }

        setState(row, state);
    }

    public BeanState getState() {
        int row = view.getSelectedRow();
        if (row < 0) {
            return null;
        }

        return getState(row);
    }

    public BeanState getState(int row) {
        HbnWinBean wb = (HbnWinBean) data.get(row);
        return wb.getState();
    }


    public void setValueAt(Object value, int row, int col) {
        int rRow = row;
        int rCol = col;

        HbnWinBean wb = (HbnWinBean) data.get(rRow);

        try {
            String prop= MapUtils.getString(colFieldMap,shownTitleNames[col]);
            if (wb.getState() != BeanState.New) {
                Object oldValue = PropertyUtils.getProperty(wb, prop);
                if (!isEqual(prop, oldValue, value)) {
                    wb.setState(BeanState.Modified);
                }
            }
            BeanUtils.setProperty(wb, prop, value);
        } catch (IllegalAccessException e) {
            log.error(e);
            Msg.showMsg(null, e.getMessage());
        } catch (InvocationTargetException e) {
            log.error(e);
            Msg.showMsg(null, e.getMessage());
        } catch (NoSuchMethodException e) {
            log.error(e);
            Msg.showMsg(null, e.getMessage());
        }

        row = view.convertRowIndexToView(row);
        fireTableRowsUpdated(row, row);
    }

    public void insert(HbnWinBean wb) {
        wb.setState(BeanState.New);
        data.add(wb);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
        selectRow(data.size() - 1);
        int col = getFirstCol();
        if (col != -1) {
            view.editCellAt(data.size() - 1, col);

            if (view.getCellEditor() instanceof DefaultCellEditor) {
                DefaultCellEditor edit = (DefaultCellEditor) view.getCellEditor();
                edit.getComponent().requestFocus();
            }

        }
    }

    public Object get(int i) {
        if (i > data.size() - 1) {
            return null;
        }

        return data.get(i);
    }

    public Object get() {
        return get(view.getSelectedRow());
    }

    public void removeFromModel(int i) {
        data.remove(i);
        fireTableRowsDeleted(i, i);
    }

    public void removeFromModel(Object obj) {
        int i = data.indexOf(obj);
        if (i >= 0) {
            removeFromModel(i);
        }
    }

    public void selectRow(int r) {
        view.grabFocus();
        view.changeSelection(r, 1, false, false);
    }

    public void setColWidth(int idx, int width) {
        TableColumn col = view.getColumnModel().getColumn(idx);
//        col.sizeWidthToFit();
        col.setPreferredWidth(width);
        col.setMaxWidth(width + 5);
////        col.setMaxWidth(width);
//        col.setPreferredWidth(width);
    }

    public void setEditor(int col, TableCellEditor editor) {
        view.getColumnModel().getColumn(col).setCellEditor(editor);
    }

    public void setRender(int col, TableCellRenderer render) {
        view.getColumnModel().getColumn(col).setCellRenderer(render);
    }

    public int getFirstCol() {
        for (int i = 0; i < view.getColumnCount(); i++) {
            if (isCellEditable(0, i)) {
                return i;
            }
        }

        return -1;
    }

    public void completeEdit(int row) {
        if (row + 1 > data.size()) {
            return;
        }

        for (int i = 0; i < view.getColumnCount(); i++) {
            if (isCellEditable(row, i) && view.getCellEditor(row, i) != null) {
                view.getCellEditor(row, i).stopCellEditing();
                return;
            }
        }
    }

    public boolean isEqual(String field, Object oldVal, Object newVal) {
        if (oldVal == null && newVal == null) {
            return true;
        }

        if (oldVal == null && newVal != null) {
            return compareToNull(newVal, field);
        } else if (oldVal != null && newVal == null) {
            return compareToNull(oldVal, field);
        } else {
                return oldVal.equals(newVal);
        }
    }

    public boolean compareToNull(Object val, String field) {
        if (String.class== val.getClass()) {
            String strVal = (String) val;
            if (strVal.trim().length() == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public JTable getView() {
        return view;
    }

    public String getDelCol() {
        return delCol;
    }

    public void setDelCol(String delCol) {
        this.delCol = delCol;
    }

    public int getDeletedVal() {
        return deletedVal;
    }

    public void setDeletedVal(int deletedVal) {
        this.deletedVal = deletedVal;
    }

    public String getDelField() {
        return delField;
    }

    public void setDelField(String delField) {
        this.delField = delField;
    }

    public void addColFieldMap(String colName,String fieldName){
        if (colFieldMap.containsKey(colName)){
            log.debug(colName+" has existed in column field map, check it again to avoid duplicated assign");
            return;
        }

        colFieldMap.put(colName,fieldName);
    }

}

