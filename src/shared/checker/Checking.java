package shared.checker;

import javax.swing.JOptionPane;
import java.util.Vector;
import database_config.DBconnector;

public class Checking {
	public static boolean IsNull(String str) {
		if (str.trim().equals("") || str.trim().equals(null))
			return true;
		else
			return false;
	}

	public static boolean IsLetter(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (Character.isLetter(str.charAt(0)))
				return true;
		}
		return false;
	}

	public static boolean IsValidName(String str) {
		if (IsNull(str) || str.startsWith(" "))
			return true;
		if (!IsLetter(str))
			return true;
		return false;
	}

	public static boolean IsAllDigit(String str) {
		for (int i = 0; i < str.length(); i++)
			if (Character.isLetter(str.charAt(i)))
				return false;
		return true;
	}

	public static boolean checktxtprice(String strqp) {
		if (IsNull(strqp)) {
			return false;
		} else if (!IsAllDigit(strqp)) {
			return false;
		} else if (Long.parseLong(strqp) > 100000000) {
			return false;
		} else
			return true;
	}

	public static boolean checktxtquantity(String strqp) {
		if (IsNull(strqp)) {
			return false;
		} else if (!IsAllDigit(strqp)) {
			return false;
		} else if (Long.parseLong(strqp) < 0) {
			return false;
		} else if (Long.parseLong(strqp) > 10000) {
			return false;
		}
		return true;
	}

	public static boolean checksalequantity(String strqp, String id) {
		if (IsNull(strqp))
			return false;
		else if (!IsAllDigit(strqp))
			return false;
//        else
//        {
//            mySQLQueries mysql=new mySQLQueries();
//            String q=mysql.getItemData(id)[5];
//            if(Integer.parseInt(strqp)>Integer.parseInt(q))
//                return false;
//            else
//                return true;
//        }
		return false;
	}

	public static boolean IsContain(String s, Vector str) {
		for (int i = 0; i < str.size(); i++)
			if (s.equals((String) str.elementAt(i)))
				return true;
		return false;
	}

	public static String Sumamount(Vector data, int t) {
		long sum = 0;
		for (int i = 0; i < data.size(); i++) {
			sum += Long.parseLong(data.elementAt(i).toString());
		}

		if (t == 1) {
			int len = String.valueOf(sum).length(), index = 0;
			StringBuffer str = new StringBuffer("");
			for (int i = 0; i < len; i++) {
				if (index == 3) {
					str.append(",");
					index = 0;
					i--;
				} else {
					str.append(String.valueOf(sum).charAt(len - i - 1));
					index++;
				}
			}
			return str.reverse().toString();
		} else {
			return String.valueOf(sum);
		}
	}
}