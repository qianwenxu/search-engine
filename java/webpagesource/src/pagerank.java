import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class pagerank { 
		private static final double factor = 0.85;  
        public static void main(String[] args) { 
        	int n=50; 
        	double a[][] = new double[n][n];
        	for(int i=0;i<n;i++){
        		try {
        			BufferedReader br = new BufferedReader(new FileReader("wxdoc\\"+i));
                    String s = br.readLine();
                    //System.out.println(s);
                    String temp[] = s.split(" ");
                    //System.out.print(temp.length);
                    for(int j=0;j<temp.length-1;j++){
                    	//System.out.print(temp.length+"*");
                    	if(!temp[j].equals("")){
                    		a[Integer.parseInt(temp[j])][i] += 1.0/(temp.length-1);
                    	}
                    }
                    //System.out.println();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        	}
        	//print(a); 
        	double b[] = new double[n];
        	double bfactor[] = new double[n];
        	for(int i=0;i<n; i++){
        		b[i] = 1.0/n; 
        	}
        	for(int i=0;i<n;i++){
				bfactor[i] = b[i]*(1-factor);
	        }
	        for(int i=0;i<60;i++){
	        	//print(matrixMult(a,b));
				b = matrixAdd(matrixMult(a,b),bfactor);
				//print(b);
		    }
	        print(b);
	        System.out.print("结束");
	}
		public static double[] matrixMult(double a[][] ,double b[]){
			int n = a.length;
			double ans[] = new double[n];
			for(int i=0;i<n;i++){
				double sum = 0;
				for(int j=0;j<n;j++){
					sum+=a[i][j]*b[j]*factor;
				}
				ans[i] = sum;
			}
			return ans;
		}
		public static double[] matrixAdd(double a[] ,double b[]){
			int n = a.length;
			double ans[]= new double[n];
			for(int i=0;i<n;i++){
				ans[i] = a[i] + b[i];
			}
			return ans;
		}
		public static void print(double a[]){
			File writename = new File("pagerank"); // 相对路径，如果没有则要建立一个新的output。txt文件  
			try {
				writename.createNewFile();// 创建新文件
				BufferedWriter out = new BufferedWriter(new FileWriter(writename));
				for(int i=0;i<a.length;i++){
					out.write(a[i]+"\r\n");
				}
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}
		public static void print(double a[][]){
			for(int i=0;i<a.length;i++){
				for(int j=0;j<a[i].length;j++){
					System.out.print(a[i][j]+" ");
				}
				System.out.println();
			}
			System.out.println();
		}
}