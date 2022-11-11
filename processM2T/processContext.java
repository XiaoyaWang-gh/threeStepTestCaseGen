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
	
	//���������Ƕ�����
	private final static String fcFile = "C:\\codes\\Java\\processM2T\\focal.methods.txt";
	private final static String fcLabel0File = "C:\\codes\\Java\\processM2T\\focal.methods.label0.txt";
	
	private static BufferedReader idxbr,contbr;
	private static BufferedWriter contbw;
	
	private static StringBuffer sb = new StringBuffer("");//StringBuffer�洢ȷ��Ҫ�����context
	
	private final static int batch_size = 50000;
	
	public static void main(String[] args) throws IOException {
		//����idxFile
		idxbr = new BufferedReader(new FileReader(idxFile),300);
		//����contextFile
		contbr = new BufferedReader(new FileReader(fcFile),1000);
		//һ��ָ��ָ��idxFile�е�idx
		String  idxStr,contStr;
		//Ϊ���ܹ�������ʣ����Ǳ�����BufferedReaderת����ArrayList
		ArrayList<Integer> idxList = new ArrayList<Integer>();
		ArrayList<String> contList = new ArrayList<String>();
		while((idxStr=idxbr.readLine())!=null) {
			idxList.add(Integer.parseInt(idxStr));
		}
		while((contStr=contbr.readLine())!=null) {
			contList.add(contStr);
		}
//		System.out.print("idxFile�ĳ���");
//		System.out.println(idxList.size());
//		System.out.print("contextFile�ĳ���");
//		System.out.println(contList.size());
		
		idxbr.close();
		contbr.close();
		//��StringBufferд��input.methods.label0.txt(contextLabel0File) ���Ƿֳ�11��
		//д�ļ����Ĳ���1��Ϊtrue��ʾ׷��д��
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
		
		System.out.println("��д��fcLabel0File");
		
		//��֤StringBuffer�е������Ƿ��idxFileһ��
	}

}
