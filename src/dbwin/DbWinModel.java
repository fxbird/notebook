package dbwin;

import dbwin.render.TypeRender;
import java.awt.Color;
import javax.swing.table.*;
import javax.swing.*;
import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.lang.reflect.InvocationTargetException;
import java.awt.event.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import other.Msg;

public abstract class DbWinModel extends AbstractTableModel {
    protected List data;
    protected String keyField;
    protected String keyProperty;
    protected Connection cnn;
    protected String tableName;
    protected HashMap mapTitle2Field;
    protected String [] allTitleNames;
    private String[] shownTitleNames;
    protected String[] tblFieldNames;
    protected HashMap mapField2Prop;
    protected HashMap mapProp2Field;
    protected HashMap mapInsColType;
    protected HashMap mapUpdColType;
    protected HashMap mapField2Type;
    protected HashMap mapFieldType;
    protected MultipleMap mm= new MultipleMap();
    protected String[]  propArr;
    protected String[]  fieldArr;
    protected JTable view;
    private int [] editableCols;
    private int keyGeneWay;
    private JFrame frame;
    //与主表关联的表名及字段名二维数组，X*2的数组，X［0］＝表名，X［1］＝字段名
    private String[][] relatedTable;

    public DbWinModel(Connection cnn, JTable table, List data, String keyField, String keyProperty, String tableName, int keyGeneWay,JFrame frame) {
        this.cnn = cnn;
        this.data = data;
        this.keyField = keyField;
        this.keyProperty = keyProperty;
        this.tableName = tableName;
        this.view=table;
        this.keyGeneWay=keyGeneWay;
        this.frame=frame;

        concreteShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0,false),new AbstractAction(){
                public void actionPerformed(ActionEvent e) {
                    stopEditing();
                }
            });

    }

    public String[][] getRelatedTable() {
        return relatedTable;
    }

    public void setRelatedTable(String[][] relatedTable) {
        this.relatedTable = relatedTable;
    }

    public boolean hasRelatedTable(){
        return relatedTable!=null && relatedTable.length>0;
    }

    public void setEditorFont() {
        for (int i = 0; i < getColumnCount(); i++) {
             if (!isCellEditable(0,i)) {
                 continue;
             }
             CellEditor edit=view.getCellEditor(0,i);
             if (edit instanceof DefaultCellEditor){
                 ((DefaultCellEditor)edit).getComponent().setFont(view.getFont());
             }
         }
    }

    public void setRenderBackground(int r,int c,Color color){
        TableCellRenderer render=view.getCellRenderer(r, c);
        if (render instanceof  DefaultTableCellRenderer){
            ((DefaultTableCellRenderer)render).setBackground(color);
        } else if (render instanceof TypeRender){
             ((TypeRender)render).getComponent().setBackground(color);
        }

    }


    public void concreteShortcut(String key,Action action){
//       JComponent rp= (JComponent)this.getGlassPane();
        JRootPane rp=  frame.getRootPane();
        InputMap imap= rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap amap=rp.getActionMap();
        String mapKey ="sc-"+ new Random().nextInt();
        System.out.println(mapKey);
        imap.put(KeyStroke.getKeyStroke(key),mapKey);
        amap.put(mapKey,action);
    }

    public  void concreteShortcut( KeyStroke ks, Action action) {
        JRootPane rp=  frame.getRootPane();
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
     * @param editableCols
     */
    public void setEditableCols(int[] editableCols) {
        this.editableCols = editableCols;
    }

        public void trigEditClose() {
//        if (data.size()==0){
//            return;
//        }
        for (int i = 0; i < getColumnCount(); i++) {
            if (!isCellEditable(0,i)) {
                continue;
            }
            CellEditor edit=view.getCellEditor(0,i);
            if (edit instanceof DefaultCellEditor){
                ((DefaultCellEditor)edit).getComponent().addFocusListener(new FocusAdapter(){
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
            if (view.isEditing()){
                view.getCellEditor().stopCellEditing();
            }
//         }
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount(){
        if (shownTitleNames==null || shownTitleNames.length==0){
           Msg.showMsg(null,"No display column names array assigned!");
           return -1;
        } else {
//            System.out.println("col count="+shownTitleNames.length);
            return shownTitleNames.length;
        }
    }

    public abstract Object getValueAt(int rowIndex, int columnIndex);

    public String getColumnName(int column){
       if (shownTitleNames==null || shownTitleNames.length==0){
           Msg.showMsg(null,"No display column names array assigned!");
           return "";
        } else {
            return shownTitleNames[column];
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex){
       if (editableCols==null){
           Msg.showMsg(null,"No editable column index array for display assigned!");// 没有指定显示的可编辑的列索引数组
           return false;
        } else if (editableCols.length==0) {
           return false;
        } else {
            return Arrays.binarySearch(editableCols,columnIndex)>-1;
        }
    }

    /**
     * 设置字段、属性、Jtable列名的映射
     * 必须调用，如果列不在表格中显示，则用""代表其显示列名
     * @param fields
     * @param props
     * @param colNames
     * @throws Exception
     */
    public void setFieldPropCol(String[] fields,String [] props,String[] colNames) throws Exception {
        mm.add(fields,"f");
        mm.add(props,"p");
        mm.add(colNames,"c");
        allTitleNames =colNames;
        tblFieldNames=fields;

        mapFieldType=new HashMap();
        List fieldSeries=mm.getOneSeries("f");
        ResultSetMetaData meta=cnn.prepareStatement("select * from "+tableName).executeQuery().getMetaData();
            String fieldType="";
            for (int i = 1; i <= meta.getColumnCount(); i++) {
               switch (meta.getColumnType(i)){
                  case Types.BIGINT:
                  case Types.INTEGER:
                  case Types.DOUBLE:
                  case Types.FLOAT:
                       fieldType=Constant.TYPE_NUMBER;
                       break;
                  case Types.DATE:
                       fieldType=Constant.TYPE_DATE;
                       break;
                  case  Types.TIMESTAMP:
                       fieldType=Constant.TYPE_TIMESTAMP;
                       break;
                  case Types.VARCHAR:
                  case Types.CHAR:
                  default:
                      fieldType=Constant.TYPE_STRING;
               }

               if (fieldSeries.contains(meta.getColumnName(i))){
                   mapFieldType.put(mm.getMappedType(meta.getColumnName(i),"f","p"),fieldType);
               }
            }
    }


    public void save() throws SQLException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (isChanged()==false){
            return;
        }
        StringBuffer sqlIns = new StringBuffer("insert into "+tableName+"(");
        StringBuffer sqlUpd=new StringBuffer("update "+tableName+" set");
        StringBuffer quesMark=new StringBuffer();
        StringBuffer fNames=new StringBuffer();
        List listInsCols=new ArrayList();
        List listUpdCols=new ArrayList();

        //生成插入sql字段列表
        for (int i = 0; i < tblFieldNames.length; i++) {
             if (tblFieldNames[i].equals(keyField)){
                 //自增字段，不用
                 if (keyGeneWay==Constant.KEY_GENE_AUTOADD){
                      continue;
                 }
             }

            listInsCols.add(tblFieldNames[i]);

            if (sqlIns.toString().endsWith("(")){
                sqlIns.append(tblFieldNames[i]);
            } else {
                sqlIns.append(",").append(tblFieldNames[i]);
            }


            if (quesMark.length()==0){
               quesMark.append("?");
            } else {
               quesMark.append(",?");
            }
        }

        //生成更新sql
        for (int i = 0; i < tblFieldNames.length; i++) {
             if (tblFieldNames[i].equals(keyField)){
                 continue;
             }

            listUpdCols.add(tblFieldNames[i]);
            if (sqlUpd.toString().endsWith("set")) {
                sqlUpd.append(" " + tblFieldNames[i] + "=?");
            } else {
                sqlUpd.append(" ," + tblFieldNames[i] + "=?");
            }


        }


        listUpdCols.add(keyField);
        sqlUpd.append(" where "+keyField+"=?");
        sqlIns.append(") values (").append(quesMark).append(")");
        PreparedStatement psIns=cnn.prepareStatement(sqlIns.toString());
        PreparedStatement psUpd=cnn.prepareStatement(sqlUpd.toString());

        for (int i = 0; i < data.size(); i++) {
            WinBean elem = (WinBean) data.get(i);
            if (elem.getStatus() == Constant.MODIFIED) {
                Msg.showInConsole("update",sqlUpd);
                execSQL(psUpd,elem,listUpdCols);
                elem.setStatus(Constant.NOT_CHANGED);
            } else if (elem.getStatus()==Constant.NEW){
                Msg.showInConsole("insert",sqlIns);
                if (keyGeneWay==Constant.KEY_GENE_AUTOADD){
                    execSQL(psIns,elem,listInsCols);
                    elem.setStatus(Constant.NOT_CHANGED);
                    ResultSet rs=cnn.prepareStatement("select max("+keyField+") from "+tableName).executeQuery();
                    rs.next();
                    BeanUtils.setProperty(elem,keyProperty,rs.getString(1));
                } else if (keyGeneWay==Constant.KEY_GENE_NEXT_OF_MAX){
                    ResultSet rs=cnn.prepareStatement("select max("+keyField+") from "+tableName).executeQuery();
                    rs.next();
                    Integer newno=rs.getInt(1)+1;
                    BeanUtils.setProperty(elem,keyProperty,newno);
                    execSQL(psIns,elem,listInsCols);
                    elem.setStatus(Constant.NOT_CHANGED);
                }
            }
        }
    }

    public void delete() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SQLException {

       int[] rows=view.getSelectedRows();
       if (rows.length==0){
           return;
       }

        if (view.isEditing()){
           view.getCellEditor().stopCellEditing();
        }

        for (int i = rows.length-1; i >=0; i--) {
            int rRow = view.convertRowIndexToModel(rows[i]);
            WinBean wb = (WinBean) data.get(rRow);
            if (wb.getStatus() != Constant.NEW) {
                int id = Integer.parseInt(BeanUtils.getProperty(data.get(rRow), keyProperty));
                String sql = "delete from " + tableName + " where " + keyField + "=" + id;
                cnn.prepareStatement(sql).execute();
                if (hasRelatedTable()){
                    for (int j = 0; j < relatedTable.length; j++) {
                        String[] related = relatedTable[j];
                        sql = "delete from "+related[0]+" where "+related[1]+"="+id;
                        cnn.prepareStatement(sql).execute();
                    }
                }
            }


            data.remove(rRow);
            fireTableRowsDeleted(0,rRow);

       }
    }
    
    /**
     * logical delete, just set a mark column to a special value referring to logical delete
     * @param field mark column name
     * @return updated row amount
     * @throws NumberFormatException
     * @throws IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws NoSuchMethodException
     * @throws java.sql.SQLException
     */
    public int logicalDelete(String field, int newValue) throws NumberFormatException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SQLException {
    	  int[] rows=view.getSelectedRows();
          if (rows.length==0){
              return 0;
          }
          
          int sum=0;
          for (int i=rows.length-1;i>=0;i--){
        	  int rRow=view.convertRowIndexToModel(rows[i]);
        	   WinBean wb = (WinBean) data.get(rRow);
               if (wb.getStatus() != Constant.NEW) {
            	   int id = Integer.parseInt(BeanUtils.getProperty(data.get(rRow), keyProperty));
                   String sql = "update " + tableName + " set "+field+"="+newValue+" where " + keyField + "=" + id; 
                   cnn.prepareStatement(sql).execute();
                  
               }
               removeFromModel(rRow);
               sum++;
          }
          
          return sum;
    }

    public boolean isChanged(){
        for (int i = 0; i < data.size(); i++) {
            WinBean elem=(WinBean)data.get(i);
            if (elem.getStatus()==Constant.MODIFIED || elem.getStatus()==Constant.NEW){
                return true;
            }
        }

        return false;
    }

    /**
     * 将一个PreparedStatement赋值上，并执行sql
     * @param ps PreparedStatement对象，没有设置好各个参数的值
     * @param obj  存有ps中需要的参数的值的bean，
     * @param fieldList  要设置的参数对应的字段的名List
     */
    public void execSQL(PreparedStatement ps,Object obj,List fieldList) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (mapField2Type==null){
            mapField2Type=new HashMap();
            ResultSetMetaData meta=cnn.prepareStatement("select  * from "+tableName).executeQuery().getMetaData();
            String fieldType="";
            for (int i = 1; i <= meta.getColumnCount(); i++) {
               switch (meta.getColumnType(i)){
                  case Types.BIGINT:
                  case Types.INTEGER:
                  case Types.DOUBLE:
                  case Types.FLOAT:
                       fieldType=Constant.TYPE_NUMBER;
                       break;
                  case Types.DATE:
                       fieldType=Constant.TYPE_DATE;
                       break;
                  case  Types.TIMESTAMP:
                       fieldType=Constant.TYPE_TIMESTAMP;
                       break;
                  case Types.VARCHAR:
                  case Types.CHAR:
                  default:
                      fieldType=Constant.TYPE_STRING;
               }
               mapField2Type.put(meta.getColumnName(i),fieldType);
            }
        }

        for (int i = 0; i < fieldList.size(); i++) {
            String field = fieldList.get(i).toString();
            String fieldType=mapField2Type.get(field).toString();
            String prop = mm.getMappedType(field, "f", "p");
            if (Constant.TYPE_STRING.equals(fieldType)){
               String value=BeanUtils.getProperty(obj, prop);
//               ps.setString(i+1,value  ==null?"":(value.trim().length()==0?" ":value));
               ps.setString(i+1,value);
               System.out.printf("field:%s\n",field);
           } else if (Constant.TYPE_NUMBER.equals(fieldType)){
               ps.setString(i+1, BeanUtils.getProperty(obj, prop));
               System.out.printf("field:%s\n",field);
            } else if (Constant.TYPE_DATE.equals(fieldType)){
               ps.setDate(i+1,(Date) PropertyUtils.getProperty(obj, prop));
                System.out.printf("field:%s\n",field);
           } else if (Constant.TYPE_TIMESTAMP.equals(fieldType)){
               ps.setTimestamp(i+1,(Timestamp) PropertyUtils.getProperty(obj, prop));
                System.out.printf("field:%s\n",field);
           }
        }

        ps.execute();
    }

    /**
     * 通知模型，数据改变了，当在编辑时，要调用它。
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public void fireChanged() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
       int r=view.getSelectedRow();
        if (r>=0){
            WinBean wb=(WinBean)data.get(r);
            if (wb.getStatus()==Constant.NEW){
                return;
            }
            if ("0".equals(BeanUtils.getProperty(wb,keyProperty))){
                 wb.setStatus(Constant.NEW);
            } else {
                 wb.setStatus(Constant.MODIFIED);
            }
        }
    }

    public void setStatus(int row,int status){
        WinBean wb=(WinBean)data.get(row);
        wb.setStatus(status);
    }

    public void setStatus(int status){
        int row=view.getSelectedRow();
        if (row<0){
            return;
        }

        setStatus(row,status);
    }

    public int getStatus(){
       int row=view.getSelectedRow();
        if (row<0){
            return -28;
        }

       return getStatus(row);
    }

    public int getStatus(int row){
        WinBean wb=(WinBean)data.get(row);
        return wb.getStatus();
    }


    public void setValueAt(Object value, int row, int col) {
//        int rRow=view.convertRowIndexToModel(row);
//        int rCol=view.convertColumnIndexToModel(col);
        
        int rRow=row;
        int rCol=col;
        
        WinBean wb = (WinBean) data.get(rRow);
        String prop = mm.getMappedType(getColumnName(rCol),"c","p");

        try {
            if (wb.getStatus() != Constant.NEW) {
                Object oldValue = PropertyUtils.getProperty(wb, prop);
                if (!isEqual(prop,oldValue,value)){
                    wb.setStatus(Constant.MODIFIED);
                }
            }
            BeanUtils.setProperty(wb,prop,value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Msg.showMsg(null,e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Msg.showMsg(null,e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Msg.showMsg(null,e.getMessage());
        }
        
        row=view.convertRowIndexToView(row);
        fireTableRowsUpdated(row,row);
    }

    public void insert(WinBean wb){
        wb.setStatus(Constant.NEW);
        data.add(wb);
        fireTableRowsInserted(data.size()-1,data.size()-1);
        selectRow(data.size()-1);
        int col=getFirstCol();
        if (col!=-1){
            view.editCellAt(data.size()-1,col);

            if (view.getCellEditor() instanceof DefaultCellEditor){
                 DefaultCellEditor edit = (DefaultCellEditor) view.getCellEditor();
                edit.getComponent().requestFocus();
            }

        }
    }

    public Object get(int i){
         if (i>data.size()-1){
             return null;
         }

         return data.get(i);
    }

    public Object get(){
        return get(view.getSelectedRow());
    }
    
    public void removeFromModel(int i){
    	data.remove(i);
    	fireTableRowsDeleted(i, i);
    }
    
    public void removeFromModel(Object obj){
    	int i=data.indexOf(obj);
    	if (i>=0){
    		removeFromModel(i);
    	}
    }

    public void selectRow(int r){
//       view.setRowSelectionInterval(r, r);
       view.grabFocus();
       view.changeSelection(r,1,false,false);

//       Rectangle  rect = new Rectangle(0, view.getHeight(), 20, 20);
//                view.scrollRectToVisible(rect);
//                view.setRowSelectionInterval(r,r);
//                view.grabFocus();
//                view.changeSelection(r, 0, false, false);
    }

    public void setColWidth(int idx,int width){
        TableColumn col = view.getColumnModel().getColumn(idx);
//        col.sizeWidthToFit();
         col.setPreferredWidth(width);
        col.setMaxWidth(width+5);
////        col.setMaxWidth(width);
//        col.setPreferredWidth(width);
    }

    public void setEditor(int col, TableCellEditor editor){
       view.getColumnModel().getColumn(col).setCellEditor(editor);
    }

    public void setRender(int col, TableCellRenderer render){
        view.getColumnModel().getColumn(col).setCellRenderer(render);
    }

    public int getFirstCol(){
        for (int i = 0; i < view.getColumnCount(); i++) {
            if (isCellEditable(0,i)){
                return  i;
            }
        }

        return -1;
    }

    public void completeEdit(int row){
        if (row+1>data.size()){
            return;
        }

        for (int i = 0; i < view.getColumnCount(); i++) {
            if (isCellEditable(row,i) && view.getCellEditor(row,i)!=null){
                 view.getCellEditor(row,i).stopCellEditing();
                 return;
            }
        }
    }

    /**
     * 关闭窗口前一定要关闭，否则保存不上更新的东西
     * @throws java.sql.SQLException
     */
   public void closeConnection() throws SQLException {
       if (!cnn.isClosed())  {
           cnn.close();
       }
   }

   public boolean isEqual(String prop, Object oldVal,Object newVal){
      if (oldVal==null && newVal==null){
          return true;
      }

      if (oldVal==null && newVal!=null){
           return compareToNull(newVal,prop);
      } else if (oldVal!=null && newVal==null){
           return compareToNull(oldVal,prop);
      } else {
          if (Constant.TYPE_STRING.equals(mapFieldType.get(prop))){
              String strOld=(String)oldVal;
              String strNew=(String)newVal;
              return strOld.trim().equals(strNew.trim());
          } else {
              return oldVal.equals(newVal);
          }
      }
   }

   public boolean compareToNull(Object val,String prop){
        if (Constant.TYPE_STRING.equals(mapFieldType.get(prop))){
             String strVal=(String)val;
             if (strVal.trim().length()==0){
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
   
   

}

