package processM2T;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class processContext {

	private final static String idxFile = "C:\\codes\\Java\\processM2T\\label0.idxs.txt";
	
	private final static String contextFile = "C:\\codes\\Java\\processM2T\\input.methods.plusfc.txt";
	private final static String contextLabel0File = "C:\\codes\\Java\\processM2T\\input.methods.plusfc.label0.txt";
	
	//下面两个是对照组
	private final static String fcFile = "C:\\codes\\Java\\processM2T\\focal.methods.txt";
	private final static String fcLabel0File = "C:\\codes\\Java\\processM2T\\focal.methods.label0.txt";
	
	private static BufferedReader idxbr,contbr;
	private static BufferedWriter contbw;
	
	private static StringBuffer sb = new StringBuffer("");//StringBuffer存储确定要读入的context
	
	private final static int batch_size = 50000;
	
	public static void main(String[] args) throws IOException {
		//读入idxFile
		idxbr = new BufferedReader(new FileReader(idxFile),300);
		//读入contextFile
		contbr = new BufferedReader(new FileReader(fcFile),1000);
		//一个指针指向idxFile中的idx
		String  idxStr,contStr;
		//为了能够随机访问，下那边两个BufferedReader转化成ArrayList
		ArrayList<Integer> idxList = new ArrayList<Integer>();
		ArrayList<String> contList = new ArrayList<String>();
		while((idxStr=idxbr.readLine())!=null) {
			idxList.add(Integer.parseInt(idxStr));
		}
		while((contStr=contbr.readLine())!=null) {
			contList.add(contStr);
		}
//		System.out.print("idxFile的长度");
//		System.out.println(idxList.size());
//		System.out.print("contextFile的长度");
//		System.out.println(contList.size());
		
		idxbr.close();
		contbr.close();
		//将StringBuffer写入input.methods.label0.txt(contextLabel0File) 但是分成11次
		//写文件器的参数1设为true表示追加写入
		contbw = new BufferedWriter(new FileWriter(fcLabel0File,true),1000);
		
		for(int j=0;j<11;j++) {
			for(int i=j*batch_size;i<idxList.size()&&i<(j+1)*batch_size;i++) {
				int i2 = idxList.get(i).intValue();
				System.out.print(i2);
				sb.append(contList.get(i2)+"\n");
			}
			contbw.write(sb.toString());
			sb.setLength(0);
		}
		contbw.close();
		
		System.out.println("已写入fcLabel0File");
		
		//验证StringBuffer中的行数是否和idxFile一致
	}

}
