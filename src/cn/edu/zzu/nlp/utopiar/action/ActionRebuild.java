package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import cn.edu.zzu.nlp.readTree.SaveTree;
import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;
import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.UnicodeReader;
import edu.berkeley.nlp.PCFGLA.BerkeleyParser;

public class ActionRebuild extends ActionGraph {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        String temp = EditorBottom.textArea.getText();
        temp = temp.replaceAll("  ", " ");
        if(temp.endsWith("@#@#@# ")){
            temp = (String) temp.subSequence(0, temp.indexOf('@'));
        }
        String temPath = "out/seg.con.tmp";
        try {
            String filePath = null;
            String model = null;
            String language = null;
            HashMap<Integer, String> saveMap = null;
            HashMap<Integer, String> conMap = null;
            String savePath = null;
            if(EditorTabbedPane.getPATH().equalsIgnoreCase(EditorTabbedPane.getChinesePath())){
                filePath = "out/ch.seg.con";
                model = "data/chn_sm5.gr";
                language = "-chinese";
                saveMap = TreeParser.zhMap;
                savePath = EditorTabbedPane.CHINESE_PATH;
                conMap = TreeParser.zhConstraint;               
            }else {
                filePath = "out/en.raw.con";
                model = "data/eng_sm6.gr";
                language = "-tokenize";
                saveMap = TreeParser.engMap;
                savePath = EditorTabbedPane.ENGLISH_PATH;
                conMap = TreeParser.engConstraint;
            }
            
            //将约束条件保存
            conMap.put(TreeParser.getNow(), temp);
            save(conMap,filePath);
            
            FileOutputStream os = new FileOutputStream(temPath);
            os.write(temp.getBytes("utf-8"));
            os.close();
            String[] paras = { "-gr", model, "-inputFile",
                    temPath, "-outputFile", "out/parse.tmp", language };
            BerkeleyParser.main(paras);
            
            //拿到重建后的该句句法树
            File f  = new File("out/parse.tmp");
            FileInputStream in = new FileInputStream(f);            
            BufferedReader br = new BufferedReader(new UnicodeReader(in, "utf-8"));
            String out = "";
            out = br.readLine();
            
            //将结果保存起来
            saveMap.put(TreeParser.getNow(), out);
            SaveTree.save(savePath);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        GraphEditor editor = getEditor(e);
        TreeParser.readData(EditorTabbedPane.getPATH());
        ActionGraph.refreshTree(editor, TreeParser.getNow());
        EditorBottom.getTextArea().setText(editor.getLabelString());
    }
    
    public static void save(HashMap<Integer, String>map,String path) {
        try {
            int max = 0;
            FileOutputStream fos = new FileOutputStream(path);
            String init = "";
            fos.write(init.getBytes());
            for (Object object : map.keySet()) {
                max = Math.max(max, object.hashCode());
            }
            for (int i = 0; i <= max; i++) {
                fos = new FileOutputStream(path, true);
                if (map.containsKey(i)) {
                    System.out.print(map.get(i));
                    fos.write(((String)map.get(i)).getBytes("utf-8"));
                }
                fos.write(System.getProperty("line.separator").getBytes("utf-8"));
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public static void main(String []args){
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(1,"10");
        map.put(2, "20");
    }
}
