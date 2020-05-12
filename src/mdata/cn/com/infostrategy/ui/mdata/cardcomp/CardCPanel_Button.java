/**************************************************************************
 * $RCSfile: CardCPanel_Button.java,v $  $Revision: 1.8 $  $Date: 2012/10/08 02:22:48 $
 **************************************************************************/
package cn.com.infostrategy.ui.mdata.cardcomp;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import cn.com.infostrategy.to.mdata.ButtonDefineVO;
import cn.com.infostrategy.to.mdata.CommUCDefineVO;
import cn.com.infostrategy.to.mdata.Pub_Templet_1_ItemVO;
import cn.com.infostrategy.to.mdata.StringItemVO;
import cn.com.infostrategy.ui.common.BillDialog;
import cn.com.infostrategy.ui.common.ClientEnvironment;
import cn.com.infostrategy.ui.common.LookAndFeel;
import cn.com.infostrategy.ui.common.MessageBox;
import cn.com.infostrategy.ui.common.WLTButton;
import cn.com.infostrategy.ui.mdata.BillListDialog;
import cn.com.infostrategy.ui.mdata.BillPanel;
import cn.com.infostrategy.ui.mdata.WLTActionEvent;
import cn.com.infostrategy.ui.mdata.WLTActionListener;

/**
 * ��Ƭ�а�ť�ؼ�!!!��ť���ֻ�����Ҫ��!!
 * @author xch
 *
 */
public class CardCPanel_Button extends AbstractWLTCompentPanel {

	private static final long serialVersionUID = 1523290372757996226L;

	private Pub_Templet_1_ItemVO templetItemVO = null; //
	private String key = null;
	private String name = null;
	private String refdesc = null; //

	private JLabel label = null;
	private JButton btn = null; //
	private BillPanel billPanel = null;

	private int li_label_width = 120;
	private int li_cardheight = 20; //
	private int li_btnwidth = 75; //

	public CardCPanel_Button(Pub_Templet_1_ItemVO _templetVO) {
		super();
		this.templetItemVO = _templetVO;
		this.key = templetItemVO.getItemkey();
		this.name = templetItemVO.getItemname();
		this.refdesc = templetItemVO.getRefdesc(); //����˵��
		this.li_label_width = templetItemVO.getLabelwidth(); //li_label_width = templetItemVO.getLabelwidth();  //
		this.li_btnwidth = templetItemVO.getCardwidth().intValue(); // ���ÿ���
		this.li_cardheight = templetItemVO.getCardHeight().intValue(); //�߶�  
		initialize();

	}

	public CardCPanel_Button(Pub_Templet_1_ItemVO _templetVO, StringItemVO _initItemVO, BillPanel _billPanel) {
		super();
		this.templetItemVO = _templetVO;
		this.key = templetItemVO.getItemkey();
		this.name = templetItemVO.getItemname();
		this.refdesc = templetItemVO.getRefdesc(); //����˵��
		this.billPanel = _billPanel; //
		this.li_label_width = templetItemVO.getLabelwidth(); //
		this.li_btnwidth = templetItemVO.getCardwidth().intValue(); // ���ÿ���
		this.li_cardheight = templetItemVO.getCardHeight().intValue(); //�߶�  
		initialize();

	}

	public CardCPanel_Button(String key, String name) {
		super();
		this.key = key;
		this.name = name;
		initialize();
	}

	public CardCPanel_Button(String key, String name, int r, int c) {
		super();
		this.key = key;
		this.name = name;

		initialize();
	}

	public void initialize() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); //
		this.setBackground(LookAndFeel.cardbgcolor); //
		if (templetItemVO != null) {
			label = createLabel(templetItemVO); //���ø����ṩ�ķ�������Label
		} else {
			label = new JLabel(name); //
			label.setHorizontalAlignment(SwingConstants.RIGHT); //
			label.setVerticalAlignment(SwingConstants.TOP); //
			label.setPreferredSize(new Dimension(li_label_width, li_cardheight)); //���ÿ���
		}

		label.setText(""); //��ť��ǰ���Label���ֲ�Ҫ
		ButtonDefineVO btnVO = new ButtonDefineVO(this.getItemKey());
		btnVO.setBtntext(name); //
		CommUCDefineVO ucvo = templetItemVO.getUCDfVO();
		if (ucvo != null && ucvo.getConfValue("��ʾ����") != null) {
			btnVO.setBtntext(ucvo.getConfValue("��ʾ����", ""));
		}

		if (ucvo != null && !"".equals(ucvo.getConfValue("��ʾͼƬ", ""))) {
			btnVO.setBtnimg(ucvo.getConfValue("��ʾͼƬ", ""));
		}

		if (ucvo != null && !"".equals(ucvo.getConfValue("��ʾ", ""))) {
			btnVO.setBtntooltiptext(ucvo.getConfValue("��ʾ", ""));
		}
		btn = new WLTButton(btnVO); //������ť
		//btn.setBackground(LookAndFeel.systembgcolor); //���ñ�����ɫ,Ҫȥ��,����Ч����������ͨ��ťЧ����һ��,û���ʸ�!!!
		btn.setPreferredSize(new Dimension(li_btnwidth, li_cardheight)); //���ÿ�����߶�!!
		if (ClientEnvironment.isAdmin()) {//sunfujun/20120817/��֪Ϊ������
			btn.setToolTipText("<html>�����б�=>LAW_LAW_CODE1<br>�����б�=>LAW_LAW_CODE1;�������ݳɹ�!<br>������Ϣ=>�������ݳɹ�!<br></html>");//������ť���ֶ��塾���/2012-05-11��
		}
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onButtonClick((JButton) e.getSource()); //
			}
		}); //
		this.add(label); //
		this.add(btn); //
		this.setPreferredSize(new Dimension(li_label_width + li_btnwidth, li_cardheight)); //
	}

	/**
	 * �����ť...
	 * @param _button
	 */
	protected void onButtonClick(JButton _button) {
		try {
			String str_actionname = null;
			if (templetItemVO.getUCDfVO() != null) {
				if (!"".equals(templetItemVO.getUCDfVO().getConfValue("����¼�", ""))) {
					str_actionname = templetItemVO.getUCDfVO().getConfValue("����¼�", "");
				}
			} else {
				str_actionname = this.refdesc;
			}
			if (str_actionname != null) {
				if (str_actionname.startsWith("�����б�=>")) {//�߼��ǵ���һ���б����ڣ�ѡ��һ���¼����ȷ�������ʾ��ʾ��Ϣ����Ҫ��Ϊ��ϵͳDemo�и��ͻ��������ý�����ʾ�����/2012-05-11��
					String sub1 = str_actionname.substring(6);
					String sub2 = "";
					if (sub1.contains(";")) {
						sub2 = sub1.substring(sub1.indexOf(";") + 1);
						sub1 = sub1.substring(0, sub1.indexOf(";"));
					}
					BillDialog billdialog = new BillListDialog(this, "��ѡ��", sub1);
					billdialog.setVisible(true);
					if (billdialog.getCloseType() == 1 && !"".equals(sub2)) {
						MessageBox.show(this, sub2);
					}
				} else if (str_actionname.startsWith("������Ϣ=>")) {//�߼���ֱ����ʾ��ʾ��Ϣ����Ҫ��Ϊ��ϵͳDemo�и��ͻ��������ý�����ʾ�����/2012-05-11��
					MessageBox.show(this, str_actionname.substring(6));
				} else {
					str_actionname = str_actionname.replaceAll("\"", "");
					WLTActionListener actionListener = (WLTActionListener) Class.forName(str_actionname).newInstance(); //
					WLTActionEvent event = new WLTActionEvent(_button, billPanel, templetItemVO.getItemkey()); //
					actionListener.actionPerformed(event); //ִ�а�ť����!!
				}
			}
		} catch (Exception ex) {
			MessageBox.showException(this, ex);
		}
	}

	public String getDataType() {
		return null;
	}

	public String getItemKey() {
		return key;
	}

	public String getItemName() {
		return name;
	}

	public String getValue() {
		return btn.getText();
	}

	public void setText(String str) {
		this.btn.setText(str);
	}

	public void setValue(String _value) {
		btn.setText(_value);
	}

	public void reset() {
		//btn.setText(""); //
	}

	public void setItemEditable(boolean _bo) {
		btn.setEnabled(_bo); //
	}

	public void setItemVisiable(boolean _bo) {
		this.setVisible(_bo); //
	}

	@Override
	public boolean isItemEditable() {
		return btn.isEnabled();
	}

	public Object getObject() {
		return new StringItemVO(getValue());
	}

	public void setObject(Object _obj) {
		if (_obj != null) {
			StringItemVO itemVO = (StringItemVO) _obj;
			setValue(itemVO.getStringValue()); //
		}
	}

	public JLabel getLabel() {
		return null; //��ťû������!!
	}

	public void focus() {
		btn.requestFocus();
		btn.requestFocusInWindow();
	}

	public int getAllWidth() {
		return this.li_btnwidth; //
	}

	public JButton getButtontn() {
		return btn;
	}

}