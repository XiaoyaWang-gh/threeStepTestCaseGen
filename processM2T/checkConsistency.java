package processM2T;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class checkConsistency {
	
	//������������label0���ĵ����ж϶�Ӧid��һ����  ���Ƕ�������200M
	private final static String fcLabel0File = "C:\\codes\\Java\\processM2T\\focal.methods.label0.txt";
	private final static String contextLabel0File = "C:\\codes\\Java\\processM2T\\input.methods.plusfc.label0.txt";
	private final static String tcLabel0File  ="C:\\codes\\Java\\processM2T\\output.tests.label0.txt";
	//output.prefix����Դ��output.tests.label0
	private final static String prefixFile = "C:\\codes\\Java\\processM2T\\output.prefix.txt";
	
	private static ArrayList<String> fcArrList = new ArrayList<String>();
	private static ArrayList<String> contextArrList = new ArrayList<String>();
	private static ArrayList<String> tcArrList = new ArrayList<String>();
	private static ArrayList<String> preArrList = new ArrayList<String>();
	
	private static BufferedReader br;
	
	public static void print4than(int id) {
		String focalmethod = fcArrList.get(id);
		String context = contextArrList.get(id);
		String testcase = tcArrList.get(id);
		String prefix = preArrList.get(id);
		
		System.out.println("�����ⷽ����\n"+focalmethod);
		System.out.println("�������ġ�\n"+context);
		System.out.println("������������\n"+testcase);
		System.out.println("��prefix��\n"+prefix);
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		String arow;
		br = new BufferedReader(new FileReader(fcLabel0File), 200);
		while((arow=br.readLine())!=null) {
			fcArrList.add(arow);
		}
		br = new BufferedReader(new FileReader(contextLabel0File), 200);
		while((arow=br.readLine())!=null) {
			contextArrList.add(arow);
		}
		br = new BufferedReader(new FileReader(tcLabel0File), 200);
		while((arow=br.readLine())!=null) {
			tcArrList.add(arow);
		}
		br = new BufferedReader(new FileReader(prefixFile), 200);
		while((arow=br.readLine())!=null) {
			preArrList.add(arow);
		}
		
		print4than(225000);
	}

}
