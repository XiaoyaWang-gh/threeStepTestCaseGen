package processM2T;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class processTC {
	
	private final static String add  ="C:\\codes\\Java\\processM2T\\output.tests.txt";
	private final static String label0add  ="C:\\codes\\Java\\processM2T\\output.tests.label0.txt";
	private final static String label0addIdxs  ="C:\\codes\\Java\\processM2T\\output.tests.label0.idxs.txt";
	private final static String contextFile = "C:\\codes\\Java\\processM2T\\input.methods.txt";
	private final static String contextLabel0File = "C:\\codes\\Java\\processM2T\\input.methods.label0.txt";
	private static BufferedReader br;
	private static BufferedWriter bw,bwIdx;
	
	public static int getAssertNum(String testcase){
		int assert_num = 0;
		String target = "assert";
		int idx=-2;
		while(idx!=-1) {
			idx = testcase.indexOf(target, Math.max(0, idx+1));
			if(idx>=0)assert_num++;
		}
		return assert_num;
	}
	
	public static ArrayList<Integer> getAssertIdxArr(String testcase) {
		ArrayList<Integer> idxArr = new ArrayList<Integer>();
		String target = "assert";
		int idx=-2;
		while(idx!=-1) {
			idx = testcase.indexOf(target, Math.max(0, idx+1));
			if(idx>=0)idxArr.add(idx);
		}
		
		return idxArr;
	}
	
	public static boolean isInterLaced(String testcase) {

		String target = "assert";		
		//���ھͲ�������˼ά������
		ArrayList<Integer> idxArr  = getAssertIdxArr(testcase);
		for(int i=0;i<idxArr.size();i++) {
			int idx = idxArr.get(i).intValue();
			int idx1 = testcase.indexOf(";",idx);
			int idx2 = testcase.indexOf(target,idx1);
			if(idx == -1) {
				continue;
			}else {
				if(idx2-idx1>13) {
					return true;
				}else {
					continue;
				}
			}
		}
		
		return false;
	} 
	
	public static int getLabel(String testcase) {
		int label = 0;
		
		//�����Ƿ�Ϊ����1
		if(getAssertNum(testcase)>5)return 1;
		
		//�����Ƿ�Ϊ����2
		if(isInterLaced(testcase))return 2;
		
		return label;
	}
	
	public static void main(String[] args) throws IOException {
		//�������н�������֤ģ�飬���ǹ���ģ��
		String addStr = new File(contextLabel0File).getPath();
		long lineNum = Files.lines(Paths.get(addStr)).count();
		System.out.println("Total number of lines : " + lineNum);
		
//		br = new BufferedReader(new FileReader(add), 310);
//		
//		
//		String line;
//		StringBuffer sb= new StringBuffer("");
//		StringBuffer sbIdx= new StringBuffer("");
//		
//		int labelArr[] = new int[] {0,0,0};
//		int idx = 0;
//	    while ((line = br.readLine()) != null) {
//	    	labelArr[getLabel(line)]++;
//	    	if(getLabel(line)==0) {
//	    		sb.append(line+"\n");
//	    		sbIdx.append(idx+"\n");
//	    	}
//	    	idx ++;
//	    }
//	    br.close();
//
//		for(int j=0;j<3;j++) {
//			System.out.print(j);
//			System.out.print(" : ");
//			System.out.print(labelArr[j]);
//			System.out.print("����������\n");
//		}
//		
//		bw = new BufferedWriter(new FileWriter(label0add),300);
//		bw.write(sb.toString());
//		bw.close();
//		
//		bwIdx = new BufferedWriter(new FileWriter(label0addIdxs),300);
//		bwIdx.write(sbIdx.toString());
//		bwIdx.close();
//		
//		System.out.println("�ѽ�label==0�Ĳ�������д�����ļ�");
	}
}
