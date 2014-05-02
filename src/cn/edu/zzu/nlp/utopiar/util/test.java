package cn.edu.zzu.nlp.utopiar.util;

import java.io.IOException;

import edu.berkeley.nlp.PCFGLA.BerkeleyParser;

public class test {

	public static void main(String[] args) throws IOException{
//		BerkeleyParser parser = new BerkeleyParser();
		String[] paras = {"-gr", "data/chn_sm5.gr", "-inputFile", "data/ch.seg.con", "-outputFile", "ch.con.parse"};
		BerkeleyParser.main(paras);
	}
}
