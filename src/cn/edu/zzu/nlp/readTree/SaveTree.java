package cn.edu.zzu.nlp.readTree;

import java.io.FileOutputStream;
import java.util.HashMap;

import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;

public class SaveTree {

	public static void save(int now, String str, String path) throws Exception{
		if (path.equalsIgnoreCase(EditorTabbedPane.getChinesePath())) {
			TreeParser.zhMap.put(Integer.valueOf(now), str);
		}else {
			TreeParser.engMap.put(Integer.valueOf(now), str);
		}
	}
	
	public static boolean save(String path){	
		HashMap<Integer, String> saveMap;
		if (path.equalsIgnoreCase(EditorTabbedPane.getChinesePath())) {
			saveMap = TreeParser.zhMap;
		}else {
			saveMap = TreeParser.engMap;
		}
		try {
			FileOutputStream fos = new FileOutputStream(path);
			String init = "";
			fos.write(init.getBytes());
			fos = new FileOutputStream(path,true);
			for (int i = 0; i < saveMap.size(); i++) {
				String temp = saveMap.get(i);
				temp += System.getProperty("line.separator");
				fos.write(temp.getBytes("utf-8"));
			}
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
}
