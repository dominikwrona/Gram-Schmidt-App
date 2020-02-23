package model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections; 

public class Model {

	public boolean independentTest(ArrayList<ArrayList<Integer>> vectors) {
		// dim: the dimension of R^n
		int dim = vectors.size();
		
		int num = vectors.get(0).size();
		System.out.println("num: "+num+" dim: "+dim);
		// case: if any row is all 0's, not independent - guess we should add it for due diligence :(
		if (num > dim) {
			return false;
		}
		else if (num==dim) {
			return determinant(vectors) != 0;
		}
		else {
			ArrayList<ArrayList<Integer>> matrix = gaussianAlgorithm(vectors);

			boolean result = false;
			for (ArrayList<Integer> row:matrix) {
				int zeroes =0;
				zeroes += Collections.frequency(row, 0);
				//System.out.println("zeroes: " +zeroes);
				if (zeroes == row.size()-1 || zeroes == row.size()) {
					result = true;
				}
				else {
					return false;
				}
			}
			return result;
		}
		
	}
	/** Another option: Use cofactor expansion to determine if determinant is non-zero, i.e. if the vectors
	 * vectors are independent - this occurs when we have the trivial solution that we attempt to find
	 * in the gaussian below (Thm 3 section 5.2)
	 */
	public int determinant(ArrayList<ArrayList<Integer>> matrix) {
		int det = 0;
		// the base case
		if (matrix.size() == 2 && matrix.get(0).size() == 2) {
			int ad = matrix.get(0).get(0) * matrix.get(1).get(1);
			int bc = matrix.get(0).get(1) * matrix.get(1).get(0);
			return ad - bc;
		}
		// cofactor expansion
		else {
			ArrayList<ArrayList<Integer>> newmatrix = matrix;
			ArrayList<Integer> axis = new ArrayList<Integer>();
			for (ArrayList<Integer> row:newmatrix) {
				axis.add(row.get(0));
				row.remove(0);
			}
			//System.out.println("Check: " + newmatrix + " Axis: " + axis);
			int even = 1;
			int count = 0;
			for (int num:axis) {
				ArrayList<ArrayList<Integer>> rmatrix = new ArrayList<ArrayList<Integer>>();
				int rowcount = 0;
				for (ArrayList<Integer> row:newmatrix) {
					if (rowcount == count) {
						
						rowcount += 1;
						continue;}
					else { rmatrix.add(row); }
					rowcount += 1;
				}
				int k = determinant(rmatrix);
				det += (even*num) * k;
				even = even * -1;
				count += 1;
			}
		}
		return det;
	}
	
	/** algorithm used to determine independence (the hard way, but perhaps easier on runtime)- not fully done **/
	public ArrayList<ArrayList<Integer>> gaussianAlgorithm(ArrayList<ArrayList<Integer>> matrix) {
		for (ArrayList<Integer> row:matrix) {
			// to have Ax = 0 format
			row.add(0);
		}
		
		mainStep(matrix, 0);
		finalRecursive(matrix, matrix.size()-1);
		return matrix;
		
	}
	
	/** creates leading 1's **/ 
	public void mainStep(ArrayList<ArrayList<Integer>> matrix, int position) {
		
		
		if (position > matrix.get(0).size()-1) {
			return;
		}
		//System.out.println("position: " + position);
		int nonzero = findNonZero(matrix, position);
		//System.out.println("Nonzero: "+nonzero );
		if (nonzero > matrix.size()) {
			// take care of this case (column of all zeroes)
			mainStep(matrix, position+1);
			return;
		}
		else {
			Collections.swap(matrix, position, nonzero);
		}
		ArrayList<Integer> main = matrix.get(position);
		int header = main.get(position);
		int i = 0;
		for (Integer num:main) {
			num = num/header;
			main.set(i, num);
			i+=1;
		}
		int current = 0;
		for (ArrayList<Integer> row:matrix) {
			if (current <= position) {
				current += 1;
				continue;
			}
			else {
				int leading = row.get(position);
				int dim = 0;
				for (int num:row) {
					if (dim < position) {
						dim += 1;
					}
					else {
						int k = main.get(dim);
						num = num - (leading*k);
						row.set(dim, num);
						dim +=1;
					}
				} 
			}
		}
		if (position != matrix.size()-1) {
			System.out.println(matrix);
		mainStep(matrix, position +1);}
	}
	
	/**
	 * 
	 * @param matrix
	 * @param position
	 * @return finds the first row with a non-zero entry at position
	 */
	public int findNonZero(ArrayList<ArrayList<Integer>> matrix, int position) {
		int i = 0;
		for (ArrayList<Integer> row:matrix) {
			if (i < position) {
				i += 1;
			}
			else if (row.get(position) !=0) {
				return i;
			}
			else {
				i += 1;
			}
		}
		return i +1; // test the first column all zeroes case
	}
	/**
	 * 
	 * @param matrix in row echelon form
	 * @return matrix in reduced row echelon form
	 */
	public void finalStep(ArrayList<ArrayList<Integer>> matrix) {
		
		int rownum = 0;
		for (ArrayList<Integer> row: matrix) {
			if (rownum == 0) {
				rownum +=1;
				continue;
			}
 			int position = 0;
			int leading = row.indexOf(1);
			if (leading <0) {
				continue;
			}
			System.out.println("leading: " +leading);
			for (ArrayList<Integer> r:matrix) {
				if (position <rownum) {
					int rowindex =0;
					for (int num:r) {
						if (r.indexOf(num) < leading) {
							rowindex +=1;
							continue;
						}
						num = num - (num*row.get(leading));
						r.set(rowindex, num);
						rowindex+=1;	
					}
					position +=1;
				}
			}
			rownum +=1;
			
		}
		
	}
	public void finalRecursive(ArrayList<ArrayList<Integer>> matrix, int position) {
		// in main, starter position is prob. matrix.size()
		if (position == 0) {
			return;
		}
		int leading = matrix.get(position).indexOf(1);
		if (leading<0) {
			finalRecursive(matrix, position-1);
			return;
			}
		int rowcount = 0;
		for (ArrayList<Integer> row:matrix) {
			if (rowcount == position) {
				break;
			}
			//int num = row.get(leading);
			row.set(leading, 0);
			rowcount +=1;
		}
		finalRecursive(matrix, position-1);
	}
	
	public static void main(String[] args) {
		
		Model m = new Model();
		ArrayList<ArrayList<Integer>> k = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> row1 = new ArrayList<Integer>();
		ArrayList<Integer> row2 = new ArrayList<Integer>();
		ArrayList<Integer> row3 = new ArrayList<Integer>();
		row1.add(0); row1.add(2); row1.add(-4);
		row2.add(1); row2.add(3); row2.add(3);
		row3.add(4); row3.add(-2);row3.add(1);
		k.add(row1); k.add(row2); k.add(row3);
		
		System.out.println("Determinant: " + m.determinant(k));
		int[] nums = {1,2};
		ArrayList base = new ArrayList<>(Arrays.asList(1,2));
		ArrayList base1 = new ArrayList<>(Arrays.asList(-4,-2));
		ArrayList<ArrayList<Integer>> big = new ArrayList<ArrayList<Integer>>();
		big.add(base); big.add(base1);
		System.out.println("Determinant in a base case: " + m.determinant(big));
		
		k = new ArrayList<ArrayList<Integer>>();
		ArrayList a1 = new ArrayList<>(Arrays.asList(0,2,-4));
		ArrayList a2 = new ArrayList<>(Arrays.asList(1,3,3));
		ArrayList a3 = new ArrayList<>(Arrays.asList(4,-2,1));
		k.add(a1); k.add(a2); k.add(a3);
		System.out.println(k);
		m.gaussianAlgorithm(k);
		System.out.println("after Gaussian" + k);
		
		// check vectors (1,2,3) and (2,2,3)
		ArrayList f = new ArrayList<>(Arrays.asList(1,2)); ArrayList f1 = new ArrayList<>(Arrays.asList(2,2));
		ArrayList f2 = new ArrayList<>(Arrays.asList(3,3));
		ArrayList<ArrayList<Integer>> indtest = new ArrayList<ArrayList<Integer>>();
		indtest.add(f); indtest.add(f1); indtest.add(f2);
		ArrayList<ArrayList<Integer>> test = indtest;
		System.out.println(test);
		/*m.mainStep(test,0);
		System.out.println("Mainstep result: " + test);
		m.finalStep(test);
		System.out.println("FinalStep result: " + test);*/
		//m.independentTest(indtest);
		System.out.println(m.independentTest(indtest));
		System.out.println(indtest);
		/* (!) problem: vectors should be columns in the matrix, not rows... */
		
		ArrayList d1 = new ArrayList<>(Arrays.asList(1,0,3,4)); ArrayList d2 = new ArrayList<>(Arrays.asList(1,2,-1,-1));
		ArrayList d3 = new ArrayList<>(Arrays.asList(4,2,0,5));
		ArrayList d = new ArrayList<>(Arrays.asList(d1,d2,d3));
		System.out.println("  ");
		System.out.println("the matrix before testing: " +d);
		System.out.println("independent: " + m.independentTest(d));
		
		/**
		 * TODO: Check if final Step works just as good as final Recursive
		 */
	}
}


 