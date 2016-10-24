Implementation of feasible matrix rounding by utilizing the Ford Fulkerson algorithm.

## Description ##

There is a matrix of real numbers. The task is to round all cells of the matrix up or down to an integers, s.t. the sum of rounded cells in each row (column) is equal to the rounded sums of row (column).
Notice that rounding 'by threshold' can fail.

######Example######

* original matrix

|          | col1 | col2 | row sum |                         
|----------|------|------|---------|
| row 1    | 0.8  | 0.3  | 1.1     |
| row 2    | 5.3  | 4.3  | 9.6     |
| col. sum | 6.1    | 4.6  |         |


* feasible rounding

|          | col1 | col2 | row sum |
|----------|------|------|---------|
| row 1    | 1  | 0  | 1     |
| row 2    | 6  | 4  | 10     |
| col. sum | 7    | 4  |         |

We can formulate the problem of feasible matrix rounding as a circulation problem with lower bounds.

A flow network corresponding to the above matrix follows:

<img src="https://github.com/FilipRy/algorithms/blob/master/MatrixRounding/flow_net.png?raw=true" alt="Corresponding flow network" width="400">

By this approach the feasible matrix rounding can be computed in O(n.m) (n = #rows, m = #columns)

## Usage ##

java MatrixRounding matrixFile

The matrix file must have the following format:
* 1st line contains two integers N M (rows count, columns count)
* N lines follows: each containing M real numbers representing the cells of the matrix.

e.g. the 3 lines below represt the matrix discussed above:<br/>
2 2<br/>
0.8 0.3<br/>
5.3 4.3<br/>
