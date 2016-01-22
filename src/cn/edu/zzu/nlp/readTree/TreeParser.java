package cn.edu.zzu.nlp.readTree;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.swing.JPanel;

import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;
import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.SetLabel;
import cn.edu.zzu.nlp.utopiar.util.UnicodeReader;
import cn.edu.zzu.nlp.utopiar.util.ValidCell;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.view.mxGraph;
import com.mxgraph.model.mxCell;

public class TreeParser extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 构造语法树文本路径
     */
    public String PATH;
    
    /**
     * 叶子结点计数，构造语法树时需要设置为1
     */
    public static int countleaf = 1;
    
    /**
     * 保存句法树的总数量
     */
    public static int MAXCOUNT = 0;
    
    /**
     * 保存句法树汉语句子的总数量
     */
    public static int ZHCOUNT = 0;
    
    /**
     * 保存句法树英语句子的总数量
     */
    public static int ENGCOUNT = 0;

    /**
     * 
     */
    Stack<String> stack = new Stack<String>();

    /**
     * 记录当前句法树位置
     */
    public static int now = 0;

    /**
     * 保存叶子结点
     */
    public static List<String> leaf = new ArrayList<String>();
    
    /**
     * 保存叶子结点及其词性
     */
    public static List<String> splitList = new ArrayList<String>();
    
    public static HashMap<Integer, String> zhMap = new HashMap<Integer, String>();
    
    public static HashMap<Integer, String> engMap = new HashMap<Integer, String>();
    
    public static HashMap<Integer, String> zhConstraint = new HashMap<Integer, String>();
    
    public static HashMap<Integer, String> engConstraint = new HashMap<Integer, String>();
    
    public static HashMap<Integer, String> zhParser = new HashMap<Integer, String>();
    
    public static HashMap<Integer, String> engParser = new HashMap<Integer, String>();
    
    public static HashMap<Object, String> vertex = new HashMap<Object, String>();
    
    public static HashMap<Integer, String> selectData(String path){
        if(path.equalsIgnoreCase(EditorTabbedPane.getEnglishPath())){
            return engMap;
        }else if(path.equalsIgnoreCase(EditorTabbedPane.getChinesePath())){
            return zhMap;
        }else{
            return null;
        }
    }

    public static  Boolean readData(String path) {
        if(path.equalsIgnoreCase(EditorTabbedPane.getChinesePath())){
            zhMap.clear();
            try {
                File f  = new File(path);
                FileInputStream in = new FileInputStream(f);
                BufferedReader br = new BufferedReader(new UnicodeReader(in, "utf-8"));
                String temp;
                int i=0;
                while ((temp = br.readLine()) != null) {
                    // 过滤掉字符传中含有的“() ”
                    temp = temp.replaceAll("\\(\\) ", "");
                    zhMap.put(i, temp);
                    i++;
                }
                ZHCOUNT = i;
                MAXCOUNT = i>MAXCOUNT?i:MAXCOUNT;
                MAXCOUNT = ZHCOUNT==ENGCOUNT?ZHCOUNT:MAXCOUNT;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }else if(path.equalsIgnoreCase(EditorTabbedPane.getEnglishPath())){
            engMap.clear();
            try {
                File f  = new File(path);
                FileInputStream in = new FileInputStream(f);
                BufferedReader br = new BufferedReader(new UnicodeReader(in, "utf-8"));
                String temp;
                int i=0;
                while ((temp = br.readLine()) != null) {
                    // 过滤掉字符传中含有的“() ”
                    temp = temp.replaceAll("\\(\\) ", "");
                    engMap.put(i, temp);
                    i++;
                }
                ENGCOUNT = i;
                MAXCOUNT = i>MAXCOUNT?i:MAXCOUNT;
                MAXCOUNT = ZHCOUNT==ENGCOUNT?ENGCOUNT:MAXCOUNT;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
        
        
        return true;
    }

    public static List<String> getWord(int now , HashMap<Integer, String> map){
        List<String> wordlList = new ArrayList<String>();
        String temp = map.get(now);
        if(temp==null){
            temp="(  )";
        }
        if(String.valueOf(temp.charAt(1)).equals("(")||String.valueOf(temp.charAt(1)).equals(" "))
            temp = temp.substring(1, temp.length()-1);
        String next = "";
        String old = String.valueOf(temp.charAt(0));
        if(old.equals("(")){
            wordlList.add(old);
            old = "";
        }else if(old.equals(")")){
            return null;
            
        }else if(old.equals(" ")){
            old="";
        }
        for(int i = 1;i<temp.length(); i++){
            next = String.valueOf(temp.charAt(i));
            if(next.equals(" ")){
                if(!old.equals("")){
                    wordlList.add(old);
                }   
                old="";
                next="";
            }else if(next.equals("(")||next.equals(")")){
                if(!old.equals("")){
                    wordlList.add(old);
                }               
                wordlList.add(next);
                old="";
                next="";
            }else {
                old+=next;
            }
        }
        return wordlList;
    }
    
    public static void main(String[] args){
        TreeParser.readData("data/train.ch.parse");
        List<String> list = TreeParser.getWord(0, TreeParser.selectData("data/train.ch.parse"));
        for (String string : list) {
            System.out.print(string.getBytes());
        }
    }
    
//  public static List<String> getWord(int now, List<String> list) {
//      List<String> wordlList = new ArrayList<String>();
//      String testString = list.get(now);
//      String[] strings = testString.split(" ");
//      for (String string : strings) {
//          if (string.startsWith("(")) {
//              wordlList.add("(");
//              wordlList.add(string.substring(1));
//          } else if (string.endsWith(")")) {
//              wordlList.add(string.substring(0, string.indexOf(')')));
//              do {
//                  string = string.substring(0, string.length() - 1);
//                  wordlList.add(")");
//              } while (string.endsWith(")"));
//          } else {
//              wordlList.add(string);
//          }
//      }
//      return wordlList;
//  }

    public TreeParser(GraphEditor editor, final mxGraphComponent graphComponent, String path, boolean iszH) throws IOException {
        this.PATH = path;
//      final mxGraph graph = graphComponent.getGraph();
        try {
            File f  = new File("out/ch.seg.con");
            FileInputStream in = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new UnicodeReader(in, "utf-8"));
            String temp = "";
            int i=0;
            while ((temp = br.readLine()) != null) {
                if(temp.contains("@#@#@#")){
                    zhConstraint.put(i, temp);
                }               
                i++;
            }
            in.close();
            f = new File("out/en.raw.con");
            in = new FileInputStream(f);
            br = new BufferedReader(new UnicodeReader(in, "utf-8"));
            temp = "";
            i=0;
            while ((temp = br.readLine()) != null) {
                if(temp.contains("@#@#@#")){
                    engConstraint.put(i, temp);
                }               
                i++;
            }
            in.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        readData(PATH);
        System.out.print(PATH);
        List<String> list = getWord(0, selectData(PATH));
        leaf.clear();
        splitList.clear();
        countleaf = 1;
        vertex.clear();
        creatTree(editor, graphComponent,list, 0);
        EditorBottom.getTextArea().setText(SetLabel.setLabel());
        new mxKeyboardHandler(graphComponent);
        this.setLayout(new BorderLayout());

        ValidCell.valid(editor);

        this.add(graphComponent, BorderLayout.CENTER);
    }
    
    public static List<Object> creatTree(GraphEditor editor, final mxGraphComponent graphComponent,List<String> list,
            int vertexY) {
        vertexY += 40;
        mxGraph graph = graphComponent.getGraph();
        graphComponent.setToolTips(true);
        Object parent = graph.getDefaultParent();
        List<Object> rtList = new ArrayList<Object>();
        int pointX = 0;
        if(list.size() == 4){
            for (int i=2;i>0;i--) {
                splitList.add(list.get(i));
                if(i==2){
                    splitList.add("/");
                }else {
                    splitList.add("  ");
                }
            }
        }
        if (list.size() == 1||list.size() == 0) {
            if(list.size()==0){
                list.add("");
            }
            graph.getModel().beginUpdate();
            pointX = countleaf * 60;
            leaf.add(list.get(0));
            countleaf++;
            try {
                Object ob = graph.insertVertex(parent, null, list.get(0), pointX,
                        vertexY, 50, 30, "fontSize=18");
                vertex.put(ob, String.valueOf(countleaf-2));
                rtList.add(ob);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                graph.getModel().endUpdate();
            }
            rtList.add(pointX);
            return rtList;
        }
        List<List<String>> lists = new ArrayList<List<String>>();
        List<Integer> count = new ArrayList<Integer>();
        Stack<String> stack = new Stack<String>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(")")) {
                while (!stack.empty() && !stack.peek().equals("(")) {
                    stack.pop();
                }
                stack.pop();
            } else {
                stack.push(list.get(i));
                if (stack.size() == 3) {
                    count.add(i);
                }
            }
        }
        count.add(list.size() - 1);
        for (int i = 0; i < count.size() - 1; i++) {
            List<String> temp = new ArrayList<String>();
            for (int j = count.get(i); j < count.get(i + 1); j++) {
                temp.add(list.get(j));
            }
            lists.add(temp);
        }
        //括号对表示的句法树中出现（NP）的处理
        if(lists.size()==0){
            lists.add(new ArrayList<String>());
        }
        graph.getModel().beginUpdate();
        try {
            List<Object> os = new ArrayList<Object>();
            for (List<String> list2 : lists) {
                rtList = creatTree(editor, graphComponent,list2, vertexY + 60);
                os.add(rtList.get(0));          
                pointX += (Integer) rtList.get(1);
            }
            pointX = pointX / os.size();
            Object v1 = graph.insertVertex(parent, null, list.get(1), pointX,
                    vertexY, 50, 30, "fontSize=18");
            rtList.set(0, v1);
            rtList.set(1, pointX);
            for (Object object : os) {
                graph.insertEdge(parent, null, "", v1, object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            graph.getModel().endUpdate();
        }
        return rtList;
    }

    public static int getCountleaf() {
        return countleaf;
    }

    public static void setCountleaf(int countleaf) {
        TreeParser.countleaf = countleaf;
    }

    public static int getNow() {
        return now;
    }

    public static void setNow(int now) {
        TreeParser.now = now;
    }

    public static List<String> getLeaf() {
        return leaf;
    }

    public static List<String> getSplitList() {
        return splitList;
    }

    public static int getMAXCOUNT() {
        return MAXCOUNT;
    }

    
}
