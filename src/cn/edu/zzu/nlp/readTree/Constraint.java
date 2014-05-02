package cn.edu.zzu.nlp.readTree;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Constraint {
	

	public static int leafCount = 0;
	public static String constraint = "";
	
	public static Point creatTree(List<String> list) {
		Point point = new Point();
		if (list.size() == 1||list.size() == 0) {
			if(list.size()==0){
				list.add("");
			}
			point.x=leafCount;
			point.y=point.x+1;
			leafCount++;
			return point;
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
		List<Point> points = new ArrayList<Point>();
		for (List<String> list2 : lists) {
			point = creatTree(list2);
			points.add(point);
		}
		for (Point p : points) {
			point.x = Math.min(point.x, p.x);
			point.y = Math.max(point.y, p.y);
		}
//		Constraint.constraint += list.get(1)+"["+(int)point.x+","+(int)point.y+"] ";
		Constraint.constraint += "["+(int)point.x+","+(int)point.y+"] ";
		return point;
	}
	
	public static void main(String[] args){
		TreeParser.readData("data/train.ch.parse");
		List<String> list = TreeParser.getWord(0, TreeParser.selectData("data/train.ch.parse"));
		leafCount = 0;
		creatTree(list);
		System.out.print(Constraint.constraint);
	}
}
