package cn.edu.zzu.nlp.readTree;

import java.awt.BorderLayout;
import java.awt.Color;
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
import cn.edu.zzu.nlp.utopiar.util.Preferences;
import cn.edu.zzu.nlp.utopiar.util.UnicodeReader;
import cn.edu.zzu.nlp.utopiar.util.Util;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.view.mxGraph;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxRectangle;

public class TreeParser extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int MARGIN = 1000;

    /**
     * 构造语法树文本路径
     */
    public String path;
    
    /**
     * 叶子结点计数，构造语法树时需要设置为1
     */
    public int countleaf = 1;
    
    /**
     * 保存句法树的总数量
     */
    public int maxCount = 0;
    
    public int count = 0;

    /**
     * 
     */
    Stack<String> stack = new Stack<String>();

    /**
     * 保存叶子结点
     */
    public List<String> leaf = new ArrayList<String>();
    
    /**
     * 保存叶子结点及其词性
     */
    public List<String> splitList = new ArrayList<String>();
    
    public HashMap<Integer, String> map = new HashMap<Integer, String>();
   
    public HashMap<Integer, String> constraint = new HashMap<Integer, String>();
    
    public HashMap<Object, String> vertex = new HashMap<Object, String>();
    
    public Boolean readData() {
        map.clear();
        try {
            File f  = new File(path);
            FileInputStream in = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new UnicodeReader(in, "utf-8"));
            String temp;
            int i=0;
            while ((temp = br.readLine()) != null) {
                // 过滤掉字符传中含有的“() ”
                temp = temp.replaceAll("\\(\\) ", "");
                map.put(i, temp);
                i++;
            }
            count = i;
            maxCount = i>maxCount?i:maxCount;
            //maxCount = ZHCOUNT==ENGCOUNT?ZHCOUNT:maxCount;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    public List<String> getWord(int now , HashMap<Integer, String> map){
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
        //TreeParser.readData("data/train.ch.parse");
        //List<String> list = TreeParser.getWord(0, TreeParser.selectData("data/train.ch.parse"));
        //for (String string : list) {
        //    System.out.print(string.getBytes());
        //}
    }
    
    public TreeParser(GraphEditor editor, final mxGraphComponent graphComponent, String path, boolean iszH) throws IOException {
        this.path = path;
        this.editor = editor;
//      final mxGraph graph = graphComponent.getGraph();
        try {
            String filename = (iszH ? "out/ch.seg.con" : "out/en.raw.con");
            File f = new File(filename);
            FileInputStream in = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new UnicodeReader(in, "utf-8"));
            String temp = "";
            int i=0;
            while ((temp = br.readLine()) != null) {
                if(temp.contains("@#@#@#")){
                    constraint.put(i, temp);
                }               
                i++;
            }
            in.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        readData();
        System.out.print(path);
        List<String> list = getWord(0, map);
        leaf.clear();
        splitList.clear();
        countleaf = 1;
        vertex.clear();
        creatTree(editor, graphComponent,list, Preferences.DEFAULT_OFFSET_Y);
        int scrollValue = (int)(TreeParser.MARGIN * graphComponent.getGraph().getView().getScale());
        graphComponent.getHorizontalScrollBar().setValue(scrollValue);

        editor.updateBottomTextArea();
        new mxKeyboardHandler(graphComponent);
        this.setLayout(new BorderLayout());

        editor.validCells(graphComponent);

        this.add(graphComponent, BorderLayout.CENTER);
    }
    
    public List<Object> creatTree(GraphEditor editor, final mxGraphComponent graphComponent,List<String> list,
            int vertexY) {
        Integer verticalInterBoxGap = Preferences.getInstance().getVerticalInterBoxGap();
        vertexY += ( verticalInterBoxGap == null ? Preferences.DEFAULT_VERT_INTERBOX_GAP : verticalInterBoxGap.intValue() );
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
            Integer boxWidth = Preferences.getInstance().getBoxWidth();
            Integer boxHeight = Preferences.getInstance().getBoxHeight();
            Integer horizontalInterBoxGap = Preferences.getInstance().getHorizontalInterBoxGap();
            pointX = countleaf * ( (boxWidth == null ? Preferences.DEFAULT_BOX_WIDTH : boxWidth.intValue() ) + ( horizontalInterBoxGap == null ? Preferences.DEFAULT_HORIZ_INTERBOX_GAP : horizontalInterBoxGap ) );
            leaf.add(list.get(0));
            countleaf++;
            try {
                StringBuilder strStyle = new StringBuilder();
                Integer boxFontSize = Preferences.getInstance().getBoxFontSize();
                if( boxFontSize != null )
                    strStyle.append( "fontSize=" + boxFontSize + ";" );
                Color boxBackgroundColor = Preferences.getInstance().getBoxBackgroundColor();
                if( boxBackgroundColor != null ) 
                    strStyle.append( "fillColor=" + Util.colorRGBToHex( boxBackgroundColor ) + ";" );
                Color boxForegroundColor = Preferences.getInstance().getBoxForegroundColor();
                if( boxForegroundColor != null ) 
                    strStyle.append( "fontColor=" + Util.colorRGBToHex( boxForegroundColor ) + ";" );
                Color boxBorderColor = Preferences.getInstance().getBoxBorderColor();
                if( boxBorderColor != null ) 
                    strStyle.append( "strokeColor=" + Util.colorRGBToHex( boxBorderColor ) + ";" );
                Object ob = graph.insertVertex(parent, null, list.get(0), pointX + MARGIN,
                        vertexY, boxWidth == null ? Preferences.DEFAULT_BOX_WIDTH : boxWidth.intValue(), boxHeight == null ? Preferences.DEFAULT_BOX_HEIGHT : boxHeight.intValue(), strStyle.toString());
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
                if(!stack.empty())
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
            StringBuilder strStyle = new StringBuilder();
            Integer boxFontSize = Preferences.getInstance().getBoxFontSize();
            if( boxFontSize != null )
                strStyle.append( "fontSize=" + boxFontSize + ";" );
            Color boxBackgroundColor = Preferences.getInstance().getBoxBackgroundColor();
            if( boxBackgroundColor != null ) 
                strStyle.append( "fillColor=" + Util.colorRGBToHex( boxBackgroundColor ) + ";" );
            Color boxForegroundColor = Preferences.getInstance().getBoxForegroundColor();
            if( boxForegroundColor != null ) 
                strStyle.append( "fontColor=" + Util.colorRGBToHex( boxForegroundColor ) + ";" );
            Color boxBorderColor = Preferences.getInstance().getBoxBorderColor();
            if( boxBorderColor != null ) 
                strStyle.append( "strokeColor=" + Util.colorRGBToHex( boxBorderColor ) + ";" );
            Integer boxWidth = Preferences.getInstance().getBoxWidth();
            Integer boxHeight = Preferences.getInstance().getBoxHeight();

            List<Object> os = new ArrayList<Object>();
            for (List<String> list2 : lists) {
                rtList = creatTree(editor, graphComponent,list2, vertexY + ( boxHeight == null ? Preferences.DEFAULT_BOX_HEIGHT : boxHeight.intValue() ));
                os.add(rtList.get(0));          
                pointX += (Integer) rtList.get(1);
            }
            pointX = pointX / os.size();
            Object v1 = graph.insertVertex(parent, null, list.get(1), pointX + MARGIN,
                    vertexY, boxWidth == null ? Preferences.DEFAULT_BOX_WIDTH : boxWidth.intValue(), boxHeight == null ? Preferences.DEFAULT_BOX_HEIGHT : boxHeight.intValue(), strStyle.toString());
            rtList.set(0, v1);
            rtList.set(1, pointX);
            for (Object object : os) {
                strStyle = new StringBuilder("");
                Color edgeColor = Preferences.getInstance().getEdgeColor();
                if( edgeColor != null ) 
                    strStyle.append( "strokeColor=" + Util.colorRGBToHex( edgeColor ) + ";" );
                graph.insertEdge(parent, null, "", v1, object, strStyle.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            graph.getModel().endUpdate();
        }
        return rtList;
    }

    public int getCountleaf() {
        return countleaf;
    }

    public void setCountleaf(int countleaf) {
        this.countleaf = countleaf;
    }

    public List<String> getLeaf() {
        return leaf;
    }

    public List<String> getSplitList() {
        return splitList;
    }

    public int getMaxCount() {
        return maxCount;
    }

    private GraphEditor editor;

}
