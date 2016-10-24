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


## Usage ##

