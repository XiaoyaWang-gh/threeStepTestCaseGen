package processM2T;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class devidePreAndOra {
	
	private final static String tcLabel0File  ="C:\\codes\\Java\\processM2T\\output.tests.label0.txt";
	private final static String prefixFile = "C:\\codes\\Java\\processM2T\\output.prefix.txt";

	private static BufferedReader br;
	private static BufferedWriter bw;
	static StringBuffer sb= new StringBuffer("");
	
	public static void addPrefix(String _tcStr) {
		//�ҵ���һ��������ŵ�idx - idxL
		int idxL = _tcStr.indexOf("{");
		//�ҵ���һ��assert��idx - idxA
		int idxA = _tcStr.indexOf("ssert");//��Assert��assert�������
		//�ҵ����idxǰ�����һ���ֺŵ�idx - idxS
		int idxS = _tcStr.lastIndexOf(";",idxA);
		//��ȡ��idxL �� idxS�Ĳ��֣����ұգ�ȥ��ͷ���ո�add��sb��
		if(idxS>idxL&&idxS<_tcStr.length()) {
			String preStr =  _tcStr.substring(idxL+1, idxS+1);
			preStr = preStr.trim();
			sb.append(preStr+"\n");
		}else {
			System.out.println(_tcStr);
			System.out.println("��substringû����ѽ"+" idxL:"+idxL+" idxA:"+idxA+" idxS:"+idxS+" length:"+_tcStr.length());
		}
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		br = new BufferedReader(new FileReader(tcLabel0File), 310);
		String tcStr = "";
		while((tcStr=br.readLine())!=null) {
			addPrefix(tcStr);
		}
		
		br.close();
		
		bw = new BufferedWriter(new FileWriter(prefixFile),300);
		bw.write(sb.toString());
		bw.close();
	}

}
