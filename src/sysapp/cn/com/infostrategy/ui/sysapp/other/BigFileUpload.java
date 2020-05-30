package cn.com.infostrategy.ui.sysapp.other;

import cn.com.infostrategy.bs.common.RemoteCallServlet;
import cn.com.infostrategy.to.common.HashVO;
import cn.com.infostrategy.to.common.TBUtil;
import cn.com.infostrategy.to.common.WLTLogger;
import cn.com.infostrategy.to.mdata.InsertSQLBuilder;
import cn.com.infostrategy.to.mdata.RefItemVO;
import cn.com.infostrategy.ui.common.*;
import cn.com.infostrategy.ui.mdata.BillListHtmlHrefEvent;
import cn.com.infostrategy.ui.mdata.BillListHtmlHrefListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * zzl 大文本明细上传
 * 也可以封装成通用的大文本上传方式
 *
 */
public class BigFileUpload extends AbstractWorkPanel implements BillListHtmlHrefListener, ActionListener {
    WLTButton excel_btn = new WLTButton("数据导入");
    private String count = TBUtil.getTBUtil().getSysOptionStringValue("大文本数据上传参数", "");//zzl[2020-5-26]
    //zzl tablename=表名 fileType=文件格式
    private String tableName,fileType;
    private String selectDate = "";
    private String colLength = "";//列的值
    private String strFg="";//文本分隔符合
    private String xcCount="";//线程数
    private ExecutorService executor;
    private int countjv=0;
    private Logger logger = WLTLogger.getLogger(RemoteCallServlet.class);
    public void initialize() {
        String [] str=count.split(",");
        tableName=str[0].toString();
        fileType=str[1].toString();
        colLength=str[2].toString();
        strFg=str[3].toString();
        xcCount=str[4].toString();
        this.setLayout(new FlowLayout(0));
        this.excel_btn.setPreferredSize(new Dimension(100, 50));
        this.excel_btn.addActionListener(this);
        this.add(this.excel_btn);
    }

    @Override
    public void onBillListHtmlHrefClicked(BillListHtmlHrefEvent _event) {

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource()==this.excel_btn){
            fileUpload();
        }

    }
    public void fileUpload(){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("请选择一个"+fileType+"文件");
        chooser.setApproveButtonText("选择");

        FileFilter filter = new FileNameExtensionFilter(""+fileType+"工作表", fileType);
        chooser.setFileFilter(filter);
        int flag = chooser.showOpenDialog(this);
        if (flag != JFileChooser.APPROVE_OPTION || chooser.getSelectedFile() == null) {
            return;
        }
        final String str_path = chooser.getSelectedFile().getAbsolutePath();

        if (!(str_path.toLowerCase().endsWith("."+fileType) || str_path.toLowerCase().endsWith("."+fileType))) {
            MessageBox.show(this, "请选择一个"+fileType+"文件!");
            return;
        }
        long startTime = System.currentTimeMillis(); // 获取开始时间
        try{
            String [] datas=getDate(this);
            selectDate=datas[0].toString();
            if(datas.toString().equals("1") || Integer.parseInt(datas[1].toString())==1){
                new SplashWindow(this, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        bigFileRead(str_path,selectDate);
                    }
                });
            }else{
                return;
            }

        }catch (Exception e){

        }
        long endTime = System.currentTimeMillis(); // 获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
    }
    public void bigFileRead(String str_path,String data){
        try{
            File file = new File(str_path);
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "utf-8"), 5 * 1024 * 1024);// 用5M的缓冲读取文本文件
            String line;
            int counter = 0;
            final List<String> list=new ArrayList<String>();
            final BIgFileUploadIfc service = (BIgFileUploadIfc) UIUtil
                    .lookUpRemoteService(BIgFileUploadIfc.class);
            String[] colnum=null;
            String newtableName=tableName+"_"+data.replace("-","");
            HashVO [] vos=UIUtil.getHashVoArrayByDS(null,"select * from dba_tables where TABLE_NAME='"+newtableName.toUpperCase()+"'");
            if(vos.length>0){
                int count=MessageBox.showConfirmDialog(this,"日期"+data+"的数据已存在，点击确定覆盖，点击关闭取消");
                if(count==0){
                    UIUtil.executeBatchByDS(null,new String [] {"drop table "+newtableName});
                }else{
                    return;
                }
            }
            executor = Executors.newFixedThreadPool(Integer.parseInt(xcCount));
            while (!StringUtils.isEmpty(line = reader.readLine())) {
                if (counter == 0) {
                    colnum = line.split(strFg);
                    String sql=creatTable(colnum,newtableName);
                    list.add(sql);
                    countjv=service.upLoad(list);
                    if(countjv==0){
                        MessageBox.show(this,"导入失败");
                        return;
                    }
                    list.clear();
                }else{
                    list.add(insertSql(colnum,line,newtableName));
                }
                if(list.size()==2000){
                    final List<String> newList=new ArrayList<String>();
                    newList.addAll(list);
                    list.clear();
                    executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            countjv=service.upLoad(newList);
                            newList.clear();
                        }
                    });

                }
                counter++;
            }

        if(list.size()>0){
            final List<String> newList=new ArrayList<String>();
            newList.addAll(list);
            list.clear();
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    countjv=service.upLoad(newList);
                    newList.clear();
                }
            });
        }
            executor.shutdown();
            while (true) {
                if (executor.isTerminated() && countjv>0) {
                    MessageBox.show(this,"导入成功");
                    reader.close();
                    break;
                }
                Thread.sleep(200);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 建表语句
     * @param strs
     * @param tablename
     * @return
     */
    private String creatTable(String[] strs,String tablename) {
        StringBuffer sb = new StringBuffer();
        String dbtype = ClientEnvironment.getInstance().getDefaultDataSourceType();
        sb.append("create table " + tablename + " (");
        for (int i = 0; i < strs.length; i++) {
            String l = "" + getColumnName(i);
            if ("Oracle".equalsIgnoreCase(dbtype)) {
                if ((i + 1) == strs.length) {
                    if (l.equals("AT") || l.equals("AS") || l.equals("BY") || l.equals("OF") || l.equals("ON")) {
                        sb.append("\"" + l + "\"" + " varchar(" + Integer.parseInt(colLength) + ") ");
                    } else {
                        sb.append(l + " varchar(" + Integer.parseInt(colLength) + ") ");
                    }
                } else {
                    if (l.equals("AT") || l.equals("AS") || l.equals("BY") || l.equals("OF") || l.equals("ON")) {
                        sb.append("\"" + l + "\"" + " varchar(" + Integer.parseInt(colLength) + "), ");
                    } else {
                        sb.append(l + " varchar(" + Integer.parseInt(colLength) + "), ");
                    }
                }
            }else{
                if ((i + 1) == strs.length) {
                    sb.append(l + " varchar(" + Integer.parseInt(colLength) + ") ");
                }else{
                    sb.append(l + " varchar(" + Integer.parseInt(colLength) + "), ");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     *   //得到所有的列并且转换成A B C.......
     * @param index
     * @return
     */
      private String getColumnName(int index) {
        String colCode = "";
        char key = 'A';
        int loop = index / 26;
        if (loop > 0) {
            colCode += getColumnName(loop - 1);
        }
        key = (char) (key + index % 26);
        colCode += key;
        return colCode;
    }

    /**
     * 时间
     * @param _parent
     * @return
     */
    private String [] getDate(Container _parent) {
        String [] str=null;
        int a=0;
        try {
            RefDialog_Month chooseMonth = new RefDialog_Month(_parent, "请选择上传数据的月份", new RefItemVO(selectDate, "", selectDate), null);
            chooseMonth.initialize();
            chooseMonth.setVisible(true);
            selectDate = chooseMonth.getReturnRefItemVO().getName();
            a=chooseMonth.getCloseType();
            str=new String[]{selectDate,String.valueOf(a)};
            return str;
        } catch (Exception e) {
            WLTLogger.getLogger(BigFileUpload.class).error(e);
        }
        return new String[]{"2013-08",String.valueOf(a)};
    }



    /**
     * 封装insertSQL语句
     * @param list
     * @param colnum
     * @param str
     */
    private String insertSql(String [] colnum,String str,String tablename){
        InsertSQLBuilder insert=new InsertSQLBuilder(tablename);
        String [] strValue=str.split(strFg);
        try{
            for(int i=0;i<strValue.length;i++){
                if(getColumnName(i).equals("AT") || getColumnName(i).equals("AS") || getColumnName(i).equals("BY") || getColumnName(i).equals("OF") || getColumnName(i).equals("ON")){
                    insert.putFieldValue("\""+getColumnName(i).toString()+"\"",strValue[i].toString());
                }else{
                    insert.putFieldValue(getColumnName(i).toString(),strValue[i].toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return insert.getSQL();
    }
}
