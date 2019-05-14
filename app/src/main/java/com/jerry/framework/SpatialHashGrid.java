package com.jerry.framework;

import android.util.FloatMath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 9. 12. 2014.
 */
public class SpatialHashGrid {
    List<GameObject>[] dynamicCells;
    List<GameObject>[] staticCells;
    int cellsPerRow;
    int cellsPerCol;
    float cellSize;
    /*
    The cellIds array is a working
    array that we can use to store the four cell IDs a GameObject is contained in temporarily.
    If it is
    contained in only one cell, then only the first element of the array will be set to the cell ID of the
    cell that contains the object entirely. If the object is contained in two cells, then the first two
    elements of that array will hold the cell ID, and so on. To indicate the number of cell IDs, we set
    all “empty” elements of the array to –1
     */
    int[] cellIds = new int[4];
    /*The foundObjects list is also a working list, which we can
    return upon a call to getPotentialColliders()
    */
    List<GameObject> foundObjects;

    @SuppressWarnings("unchecked")
    public SpatialHashGrid(float worldWidth, float worldHeight, float cellSize){
        this.cellSize = cellSize;
        this.cellsPerRow = (int) FloatMath.ceil(worldWidth / cellSize);
        this.cellsPerCol = (int) FloatMath.ceil(worldHeight / cellSize);
        int numCells = cellsPerRow * cellsPerCol;
        dynamicCells = new List[numCells];
        staticCells = new List[numCells];
        for(int i =0;i<numCells;i++){
            /*
            All the ArrayList
            instances we create will have an initial capacity of ten GameObject instances.
            We do this to avoid
            memory allocations. The assumption is that it is unlikely that one single cell will contain more
            than ten GameObject instances. As long as that is true, the array lists don’t need to be resized.
             */
            dynamicCells[i] = new ArrayList<GameObject>(10);
            staticCells[i] = new ArrayList<GameObject>(10);
        }
        foundObjects = new ArrayList<GameObject>(10);
    }

    public void insertStaticObject(GameObject obj){
        int[] cellIds = getCellIds(obj);
        int i =0;
        int cellId = -1;
        while (i <=3 && (cellId = cellIds[i++]) != -1){
            staticCells[cellId].add(obj);
        }
    }

    public void insertDynamicObject(GameObject obj) {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId = -1;
        while(i <= 3 && (cellId = cellIds[i++]) != -1) {
            dynamicCells[cellId].add(obj);
        }
    }

    public void removeObject(GameObject obj){
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId = -1;
        while(i <= 3 && (cellId = cellIds[i++]) != -1) {
            dynamicCells[cellId].remove(obj);
            staticCells[cellId].remove(obj);
        }
    }

    //We need to call this each frame before we reinsert the dynamic objects
    public void clearDynamicCells(GameObject obj){
        int len = dynamicCells.length;
        for(int i = 0; i < len ; i++){
            dynamicCells[i].clear();
        }
    }

    public List<GameObject> getPotentialColliders(GameObject obj) {
        foundObjects.clear();
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId = -1;
        while(i <= 3 && (cellId = cellIds[i++]) != -1) {
            int len = dynamicCells[cellId].size();
            for(int j = 0; j <len; j++){
                GameObject collider = dynamicCells[cellId].get(j);
                if(!foundObjects.contains(collider)){
                    foundObjects.add(collider);
                }
            }

            len = staticCells[cellId].size();
            for(int j = 0; j <len; j++){
                GameObject collider = staticCells[cellId].get(j);
                if(!foundObjects.contains(collider)){
                    foundObjects.add(collider);
                }
            }
        }
        return foundObjects;
    }

    public int[] getCellIds(GameObject obj){
        int x1 = (int) FloatMath.floor(obj.bounds.lowerLeft.x / cellSize);
        int y1 = (int) FloatMath.floor(obj.bounds.lowerLeft.y / cellSize);
        int x2 = (int) FloatMath.floor((obj.bounds.lowerLeft.x + obj.bounds.width) / cellSize);
        int y2 = (int) FloatMath.floor((obj.bounds.lowerLeft.y + obj.bounds.height) / cellSize);

        //object is in one cell
        if(x1 == x2 && y1 == y2){
            if(x1 >= 0 && x1 < cellsPerRow && y1 >=0 && y1 < cellsPerCol)
                cellIds[0] = x1+ (y1*cellsPerRow); //calculate in which cell obj is in
            else
                cellIds[0] = -1; //obj is not in any cell (outside if our game world
            cellIds[1] = -1;
            cellIds[2] = -1;
            cellIds[3] = -1;
        }
        //object is in two cells vertically
        else if(x1 == x2){
            int i = 0;
            //if obj is not outside in x dir
            if(x1 >= 0 && x1 < cellsPerRow) {
                if(y1 >=0 && y1 < cellsPerCol)
                    cellIds[i++] = x1 + y1*cellsPerRow;
                if(y2 >=0 && y2 < cellsPerCol)
                    cellIds[i++] = x1 + y2*cellsPerRow;
            }
            while( i <=3 )cellIds[i++] =-1;
        }
        //object is in two cells horizontally
        else if(y1==y2){
            int i = 0;
            //if obj is not outside in x dir
            if(y1 >= 0 && y1 < cellsPerCol) {
                if(x1 >=0 && x1 < cellsPerRow)
                    cellIds[i++] = x1 + y1*cellsPerRow;
                if(x2 >=0 && x2 < cellsPerRow)
                    cellIds[i++] = x2 + y1*cellsPerRow;
            }
            while( i <=3 )cellIds[i++] =-1;
        }
        //object is in four cells
        else {
            int i =0;
            int y1CellsPerRow = y1*cellsPerRow;
            int y2CellsPerRow = y2*cellsPerRow;
            if(x1 >= 0 && x1 < cellsPerRow && y1 >=0 && y1 < cellsPerCol)
                cellIds[i++] = x1 + y1CellsPerRow;
            if(x2 >=0 && x2 < cellsPerRow && y1 >=0 && y1 < cellsPerCol)
                cellIds[i++] = x2 + y1CellsPerRow;
            if(x2 >=0 && x2 < cellsPerRow && y2 >=0 && y2 < cellsPerCol)
                cellIds[i++] = x2 + y2CellsPerRow;
            if(x1 >=0 && x1 < cellsPerRow && y2 >=0 && y2 < cellsPerCol)
                cellIds[i++] = x1 + y2CellsPerRow;
            while( i <=3 )cellIds[i++] =-1;
        }
        return cellIds;
    }

    public float getSize(){
        return cellSize;
    }

    public int getNumberOfCells(){
        return cellsPerRow * cellsPerCol;
    }

    public int getCellsPerRow(){
        return cellsPerRow;
    }

}
