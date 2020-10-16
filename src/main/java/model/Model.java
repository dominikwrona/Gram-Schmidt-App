package model;
import java.util.ArrayList;
import java.util.Collections;

public class Model {

	private ArrayList<ArrayList<Float>> ind_vectors;

	public boolean independentTest(ArrayList<ArrayList<Float>> vectors) {
		// dim: the dimension of R^n
		//ind_vectors = new ArrayList<ArrayList<Float>>(vectors);
		ind_vectors = new ArrayList<ArrayList<Float>>();
		// java is stupidly pass by reference in this instance so need to completely fix it
		//ind_vectors.addAll(vectors);
		int num = vectors.size();
		int dim = vectors.get(0).size();
		for (ArrayList<Float>col:vectors) {
			ArrayList<Float> temp = new ArrayList<Float>(col);
			ind_vectors.add(temp);
		}
		
		System.out.println("num: "+num+" dim: "+dim);
		// case: if any row is all 0's, not independent - guess we should add it for due diligence :(
		if (num > dim) {
			return false;
		}
		else if (num==dim) {
			return determinant(ind_vectors) != 0;
		}
		else {
			ArrayList<ArrayList<Float>> matrix = gaussianAlgorithm(ind_vectors, dim);
			for (ArrayList<Float> col: matrix) {
				System.out.println(col.get(0) + " "+ col.get(1) + " " + col.get(2));
			}
			
			return finalStep(matrix);
		}
		
	}
	/** Another option: Use cofactor expansion to determine if determinant is non-zero, i.e. if the vectors
	 * vectors are independent - this occurs when we have the trivial solution that we attempt to find
	 * in the gaussian below (Thm 3 section 5.2)
	 */
	public float determinant(ArrayList<ArrayList<Float>> matrix) {
		float det = (float) 0;
		// the base case
		if (matrix.size() == 2 && matrix.get(0).size() == 2) {
			float ad = matrix.get(0).get(0) * matrix.get(1).get(1);
			float bc = matrix.get(0).get(1) * matrix.get(1).get(0);
			return ad - bc;
		}
		// cofactor expansion
		else {
			ArrayList<ArrayList<Float>> newmatrix = new ArrayList<ArrayList<Float>>();
			for (ArrayList<Float> old : matrix) {
				ArrayList<Float> novel = new ArrayList<Float>(old);
				newmatrix.add(novel);
			}
			System.out.println("Start of matrix:");
			for (ArrayList<Float> col:newmatrix) {
				for (float fl : col) {
					System.out.print(fl + " ");
				}
				System.out.print("\n ");
			}
			ArrayList<Float> axis = new ArrayList<Float>();
			for (ArrayList<Float> col:newmatrix) {
				axis.add(col.get(0));
				col.remove(0);
			}
			int even = 1;
			int count = 0;
			for (float num:axis) {
				ArrayList<ArrayList<Float>> cmatrix = new ArrayList<ArrayList<Float>>();
				int colcount = 0;
				for (ArrayList<Float> col:newmatrix) {
					if (colcount == count) {
						
						colcount += 1;
						continue;}
					else { cmatrix.add(col); }
					colcount += 1;
				}
				float k = determinant(cmatrix);
				det += (even*num) * k;
				even = even * -1;
				count += 1;
			}
		}
		return det;
	}
	
	/** algorithm used to determine independence (the hard way, but perhaps easier on runtime)- not fully done **/
	public ArrayList<ArrayList<Float>> gaussianAlgorithm(ArrayList<ArrayList<Float>> matrix, int dim) {
		//add column of zeroes for Ax =0 format
		ArrayList<Float> zeroes = new ArrayList<Float>();
		int num = dim;
		while (num >0) {
			zeroes.add((float) 0);
			num -=1;
		}
		matrix.add(zeroes);
		mainStep(matrix, 0, dim);
		return matrix;
		
	}
	/** creates leading 1's **/
	public void mainStep(ArrayList<ArrayList<Float>> matrix, int position, int dim) {
		int num = dim-position-1; //-1 because top row is already taken care of
		float[] nonzero = findNonZero(matrix,position);
		int row_to_swap = (int) nonzero[0];
		float value = nonzero[1];
		int col_position = 0;
		if (position == dim) {
			return;
		}
		if (row_to_swap == -1) {
			//matrix is all zeroes
			return;
		}
		else if (row_to_swap > 0) {
			ArrayList<Float> operations = new ArrayList<Float>();
			for (ArrayList<Float> col : matrix) {
				if (col_position < position) {
					col_position +=1;
					continue;
				}
				Collections.swap(col, position, row_to_swap);
				float temp = col.get(position);
				col.set(position, temp * (1/(float) value));
				if (col_position == position) {
					while (num>0) {
						float old_val = col.get(position+num);
						col.set(position+num, (float) 0);
						operations.add(old_val);
						num -= 1;
					}					
				}
				else {
					while (num>0) {
						float old_val = col.get(position+num);
						col.set(position+num, old_val - (operations.get(num-1) * col.get(position)) );
						num -=1;
					}}
				num = dim-position-1;
				col_position += 1;				
			}	
		}
		else {
			ArrayList<Float> operations = new ArrayList<Float>();
			for (ArrayList<Float> col : matrix) {
				if (col_position < position) {
					continue;
				}
				float temp = col.get(position);
				col.set(position, temp * (1/(float) value));
				if (col_position == position) {
					while (num>0) {
						float old_val = col.get(position+num);
						col.set(position+num, (float) 0);
						operations.add(old_val);
						num -=1;
					}					
				}
				else {
					int counter = 0;
					while (num>0) {
						float old_val = col.get(position+num);
						col.set(position+num, old_val - (operations.get(counter)* col.get(position)));
						counter +=1;
						num -=1;
					}		
				}
				num = dim-position-1;
				col_position += 1;				
			}	
		}
		
		mainStep(matrix, position+1, dim);
	}
	/** identifies the row that has the first non zero entry from the left **/
	public float[] findNonZero(ArrayList<ArrayList<Float>> matrix, int position) {
		int col = 0;
		float row = -1;
		float nonzero = -1;
		int num_col = matrix.size();
		float result[] = {0, 0};
		outer:
		for (ArrayList<Float> vector : matrix) {
			if (col < position) {
				col += 1;
				continue;
			}
			row = 0;
			// the last column of zeroes
			if (col == num_col-1) {
				row = -1;
				break;
			}
			if (col < position) {
				continue;
			}
			for (float value : vector) {
				if (row < position) {
					row +=1;
					continue;
				}
				if (value != 0) {
					nonzero = value;
					break outer;
				}
				row += 1;
			}
			col += 1;
		}
		result[0] = row; result[1] = nonzero;
		System.out.println("row: " + row + " value: " + nonzero);
		return result;
	}
	
	/**
	 * 
	 * @param matrix in row echelon form
	 * @return matrix in reduced row echelon form
	 */
	
	public boolean finalStep(ArrayList<ArrayList<Float>> matrix) {
		boolean result = true;
		int curr_leading = 0;
		for (ArrayList<Float>col:matrix) {
			int freq = Collections.frequency(col, (float)1);
			if (freq >=1) {
				int loc = col.indexOf((float)1);
				if (loc < curr_leading) {
					return false;
				}
			}
			else if (freq == 0){
				int zero_num = Collections.frequency(col, (float)0);
				int neg_zero = Collections.frequency(col, (float)-0.0);
				if ((neg_zero +zero_num) < col.size()) {
					return false;
				}
			}
		}
		return result;
	}
		
	
	public ArrayList<ArrayList<Float>> extractNums(String input, float dim) {

		String str[] = input.split("\\)");
		ArrayList<ArrayList<Float>> vectors = new ArrayList<ArrayList<Float>>();
		for (int i = 0; i < str.length; i++) {
			str[i] = str[i].substring(1);
			String temp[] = str[i].split(",");
			ArrayList<Float> vector = new ArrayList<Float>();
			int p = 0;
			while (p<dim) {
				float curr = (float) Integer.parseInt(temp[p]);
				vector.add(curr);
				p += 1;
			}
			vectors.add(vector);	
		}
		 
		return vectors;
	}
	/*public boolean spanningTest(ArrayList<ArrayList<Float>> matrix) {
		
		return false;
	}*/
	/**
	 * 
	 * @param matrix - a basis of a subspace
	 * @return an orthogonal basis 
	 */
	public ArrayList<ArrayList<Float>> GramSchmidt(ArrayList<ArrayList<Float>> matrix) {
		int dim = matrix.get(0).size();
		ArrayList<Float> f1 = matrix.get(0);
		ArrayList<ArrayList<Float>> orthogonal = new ArrayList<ArrayList<Float>>();
		orthogonal.add(f1);
		int num = 0; 
		for (ArrayList<Float> col:matrix) {
			if (num==0) {
				num +=1;
				continue;
			}
			ArrayList<Float> new_f = new ArrayList<Float>(col);
			int v_num = orthogonal.size();
			while (v_num >0) {
				ArrayList<Float> f = new ArrayList<Float>(orthogonal.get(v_num-1));
				//find dot product (& magnitude^2 of f)
				int dot_pos = 0; float dot = 0; float mag = 0;
				while (dot_pos < dim) {
					float f_value = f.get(dot_pos);
					mag += f_value * f_value;
					dot += (col.get(dot_pos) * f_value);
					dot_pos += 1;
				}
				float coefficient = dot / mag;
				//multiplying the coefficient to the respective vector
				int mult_pos = 0;
				while (mult_pos <dim) {
					float old_val = f.get(mult_pos);
					float new_val = old_val * coefficient;
					f.set(mult_pos, new_val);
					mult_pos +=1;
				}
				// subtracting from original vector (x_k)
				int sub_pos = 0;
				while (sub_pos <dim) {
					float result = new_f.get(sub_pos) - f.get(sub_pos);
					new_f.set(sub_pos, result);
					sub_pos +=1;
				}
				v_num -= 1;
			}
			orthogonal.add(new_f);
		}
		/*for (ArrayList<Float>col:orthogonal) {
			System.out.println(col.get(0) + " " + col.get(1) + " " + col.get(2));
		}*/
		
		return orthogonal;
	}
	
}


 