package com.domain.evernet.model;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.linear.GaussianSolver;

public class FragmentEquationSystem {

    final private int INVALID_EQUATION_SIZE = -1;
    final private int FULL_EQUATION_MATRIX = 1;
    final private int INVALID_METHOD_CALL = 2;

    private String id;
    private double[][] equationSystem;
    private double[] encodedFragmentValues;

    private int nbRow;
    private int nbColumn;
    int currentRow;

    boolean isFull;

    public FragmentEquationSystem(String id, int nbFragment){
        if (id == "" || nbFragment == 0)
            throw new IllegalArgumentException("One or several argument are invalid.");

        nbRow = nbFragment;
        nbColumn = nbFragment;

        this.id = id;
        this.equationSystem = new double[nbRow][nbColumn];
        this.encodedFragmentValues = new double[nbRow];

        currentRow = 0;
        isFull = false;
    }

    public String getId() {
        return id;
    }

    public boolean isFull() {
        return isFull;
    }

    public int addEquation(int[] equation,long payloadvalue){
        if(equation.length == 0)
            return INVALID_EQUATION_SIZE;

        if(this.isFull)
            return FULL_EQUATION_MATRIX;


        for(int position : equation){
            equationSystem[currentRow][position] = position;
            encodedFragmentValues[currentRow] = payloadvalue;
        }

        if(currentRow == nbRow)
            isFull = true;

        return 0;
    }

    public int gaussJordanElimination(Vector outPut){
        if(!isFull)
            return INVALID_METHOD_CALL;

        Vector valueVector = Vector.fromArray(encodedFragmentValues);
        Matrix linearSystem = Matrix.from2DArray(equationSystem);

        GaussianSolver systemSolver = new GaussianSolver(linearSystem);

        outPut = systemSolver.solve(valueVector);

        return 0;
    }

}
