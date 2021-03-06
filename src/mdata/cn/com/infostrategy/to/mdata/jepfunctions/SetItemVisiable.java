package cn.com.infostrategy.to.mdata.jepfunctions;

import java.util.Stack;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

import cn.com.infostrategy.ui.mdata.BillCardPanel;
import cn.com.infostrategy.ui.mdata.BillListPanel;
import cn.com.infostrategy.ui.mdata.BillPanel;
import cn.com.infostrategy.ui.mdata.BillPropPanel;
import cn.com.infostrategy.ui.mdata.BillQueryPanel;

/**
 * 设置某一项是否可编辑!!
 * @author xch
 *
 */
public class SetItemVisiable extends PostfixMathCommand {

	private BillPanel billPanel = null;

	/**
	 * 取得某一项的值!!
	 */
	public SetItemVisiable(BillPanel _billPanel) {
		numberOfParameters = 2; //有两个参数，其中第一个是ItemKey,第二个是"true"/"false"
		this.billPanel = _billPanel;
	}

	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);
		Object param_1 = inStack.pop();
		Object param_2 = inStack.pop();
		String str_itemKey = (String) param_2; //
		String str_editable = (String) param_1; //第二个值,应该是"true"/"false"

		if (str_itemKey != null && !str_itemKey.trim().equals("")) {
			str_itemKey = str_itemKey.trim();
			String[] str_allitems = str_itemKey.split(",");

			if (billPanel instanceof BillCardPanel) { //如果是卡片面板
				BillCardPanel cardPanel = (BillCardPanel) billPanel;
				if (str_editable.trim().equalsIgnoreCase("true")) { //如果是true,则可编辑
					for (int i = 0; i < str_allitems.length; i++) {
						cardPanel.setVisiable(str_allitems[i], true);
					}
				} else if (str_editable.trim().equalsIgnoreCase("false")) { //如果是"false"，则不可编辑!!!!
					for (int i = 0; i < str_allitems.length; i++) {
						cardPanel.setVisiable(str_allitems[i], false);
					}
				} else { //如果不小心输了别的参数，则啥都不干!!
				}
			} else if (billPanel instanceof BillListPanel) {
				//列表暂时还不知道如何处理,是不是修改Render?
			} else if (billPanel instanceof BillPropPanel) {

			} else if (billPanel instanceof BillQueryPanel) {
				BillQueryPanel queryPanel = (BillQueryPanel) billPanel;
				if (str_editable.trim().equalsIgnoreCase("true")) { //如果是true,则可编辑
					for (int i = 0; i < str_allitems.length; i++) {
						queryPanel.setVisiable(str_allitems[i], true);
					}
				} else if (str_editable.trim().equalsIgnoreCase("false")) { //如果是"false"，则不可编辑!!!!
					for (int i = 0; i < str_allitems.length; i++) {
						queryPanel.setVisiable(str_allitems[i], false);
					}
				} else { //如果不小心输了别的参数，则啥都不干!!
				}
			}
		}
		inStack.push("ok"); //因为设置值，没有实际返回值，所以就返回一个"ok"表示赋值成功了!!
	}
}
