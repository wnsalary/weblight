package cn.com.infostrategy.ui.mdata.formatcomp;
/**
 * 设置每个功能按钮是否显示(增删改查)
 */
import java.util.Stack;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

import cn.com.infostrategy.ui.mdata.BillFormatPanel;
import cn.com.infostrategy.ui.mdata.BillListPanel;
import cn.com.infostrategy.ui.mdata.BillListSelectListener;
import cn.com.infostrategy.ui.mdata.BillListSelectionEvent;
import cn.com.infostrategy.ui.mdata.BillTreePanel;
import cn.com.infostrategy.ui.mdata.BillTreeSelectListener;
import cn.com.infostrategy.ui.mdata.BillTreeSelectionEvent;

public class SetEveryBtnBarIsVisiable extends PostfixMathCommand {
	private BillFormatPanel formatpanel = null; //

	public SetEveryBtnBarIsVisiable(BillFormatPanel _billcellpanel) {
		this.formatpanel = _billcellpanel; //
		numberOfParameters = 10; //
	}

	public void run(Stack inStack) throws ParseException {
		Object par_select_isvist = inStack.pop(); //查询是否显示
		Object par_select = inStack.pop(); //查询
		Object par_delete_isvist = inStack.pop(); //删除是否显示
		Object par_delete = inStack.pop(); //删除
		Object par_update_isvist = inStack.pop(); //编辑是否显示
		Object par_update = inStack.pop(); //编辑
		Object par_insert_isvist = inStack.pop(); //新增是否显示
		Object par_insert = inStack.pop(); //新增

		final Object par_passive_panel = inStack.pop(); //被刷新的面板
		Object par_active_panel = inStack.pop(); //主面板

		if (par_passive_panel == null || par_active_panel == null) {
			return;
		}
		final String str_select_isvist = (String) par_select_isvist;
		final String str_select = (String) par_select;
		final String str_delete_isvist = (String) par_delete_isvist;
		final String str_delete = (String) par_delete;
		final String str_update_isvist = (String) par_update_isvist;
		final String str_update = (String) par_update;
		final String str_insert_isvist = (String) par_insert_isvist;
		final String str_insert = (String) par_insert;
		
		final BillListPanel billlistpanel = (BillListPanel) par_passive_panel;
		billlistpanel.getBillListBtn(str_select).setEnabled(false);
		billlistpanel.getBillListBtn(str_delete).setEnabled(false);
		billlistpanel.getBillListBtn(str_update).setEnabled(false);
		billlistpanel.getBillListBtn(str_insert).setEnabled(false);
		if (par_active_panel instanceof BillListPanel) { //如果是表型面板
			
			final BillListPanel billlist = (BillListPanel) par_active_panel;

			billlist.addBillListSelectListener(new BillListSelectListener() {
				public void onBillListSelectChanged(BillListSelectionEvent _event) {
					if (str_select_isvist.equals("false")) {
						billlistpanel.getBillListBtn(str_select).setEnabled(false);
					} else {
						billlistpanel.getBillListBtn(str_select).setEnabled(true);
					}
					if (str_delete_isvist.equals("false")) {
						billlistpanel.getBillListBtn(str_delete).setEnabled(false);
					} else {
						billlistpanel.getBillListBtn(str_delete).setEnabled(true);
					}
					if (str_update_isvist.equals("false")) {
						billlistpanel.getBillListBtn(str_update).setEnabled(false);
					} else {
						billlistpanel.getBillListBtn(str_update).setEnabled(true);
					}
					if (str_insert_isvist.equals("false")) {
						billlistpanel.getBillListBtn(str_insert).setEnabled(false);
					} else {
						billlistpanel.getBillListBtn(str_insert).setEnabled(true);
					}
				}
			});
		} else if (par_active_panel instanceof BillTreePanel) { //如果是树型面板!!!
			BillTreePanel billTree = (BillTreePanel) par_active_panel;

			billTree.addBillTreeSelectListener(new BillTreeSelectListener() {
				public void onBillTreeSelectChanged(BillTreeSelectionEvent _event) {
					if (str_select_isvist.equals("false")) {
						billlistpanel.getBillListBtn(str_select).setEnabled(false);
					} else {
						billlistpanel.getBillListBtn(str_select).setEnabled(true);
					}
					if (str_delete_isvist.equals("false")) {
						billlistpanel.getBillListBtn(str_delete).setEnabled(false);
					} else {
						billlistpanel.getBillListBtn(str_delete).setEnabled(true);
					}
					if (str_update_isvist.equals("false")) {
						billlistpanel.getBillListBtn(str_update).setEnabled(false);
					} else {
						billlistpanel.getBillListBtn(str_update).setEnabled(true);
					}
					if (str_insert_isvist.equals("false")) {
						billlistpanel.getBillListBtn(str_insert).setEnabled(false);
					} else {
						billlistpanel.getBillListBtn(str_insert).setEnabled(true);
					}
				}
			});
		}

	}
}