/*
 * Archivo     : TablaOrdenada.java
 * F. Creaci�n : 14-05-2003
 * F. Ult.Mod. : 14-05-2003
 *
 * Esta clase hereda de TablaMap
 */
package totem.utl;


import java.util.*;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;


//=======================
//Constructor de la clase
//=======================
public class TablaOrdenada extends TablaMap 
{
   int      indices[];
   Vector   columnasOrdenadas=new Vector();
   boolean  ascendente = true;
   int	    numComparaciones;

   //=============
   //Constructores
   //=============
   public   TablaOrdenada()		     { indices=new int[0];}
   public   TablaOrdenada(TableModel modelo) { setModel(modelo);}

   
   public void setModel(TableModel modelo)   { super.setModel(modelo); inicializarIndices();}
   
   private void inicializarIndices()
   {
      int numFilas = modelo.getRowCount();
      indices = new int[numFilas];
      for (int i=0; i<numFilas; i++) {indices[i]=i;}
   }


   public void addMouseListenerToHeaderInTable(JTable table) 
   { 
      final TablaOrdenada tordenada = this; 
      final JTable tableView = table; 
      tableView.setColumnSelectionAllowed(false); 
      MouseAdapter listMouseListener = new MouseAdapter() 
      {
	 public void mouseClicked(MouseEvent e) 
	 {
	    TableColumnModel columnModel = tableView.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX()); 
            int col = tableView.convertColumnIndexToModel(viewColumn); 
            if (e.getClickCount() == 1 && col!= -1) 
	    {
	       //System.out.println("Sorting ..."); 
	       int shiftPressed = e.getModifiers()&InputEvent.SHIFT_MASK; 
               boolean ascendente = (shiftPressed == 0); 
               tordenada.ordenarPorColumna(col, ascendente); 
            }
         }
      };
      JTableHeader th = tableView.getTableHeader(); 
      th.addMouseListener(listMouseListener); 
   }

   
   //=======
   //GETYSET
   //=======

   public Object  getValueAt(int f,int c) { checkModelo(); return modelo.getValueAt(indices[f], c);}
   public void	  setValueAt(Object val,int f,int c) {checkModelo(); modelo.setValueAt(val,indices[f],c);}


   
   //=========
   //ordenar()
   //=========
   public void ordenarPorColumna(int col) { ordenarPorColumna(col,true); }

   public void ordenarPorColumna(int col, boolean ascendente) 
   {
      this.ascendente = ascendente;
      columnasOrdenadas.removeAllElements();
      columnasOrdenadas.addElement(new Integer(col));
      ordenar(this);
      super.tableChanged(new TableModelEvent(this)); 
   }
   
   public void tableChanged(TableModelEvent e) {inicializarIndices(); super.tableChanged(e);}
   
   public void ordenar(Object emisor)
   {
      checkModelo();
      numComparaciones=0;
      // n2sort();
      // qsort(0, indexes.length-1);
      ordenacionMetodoShuttle( (int[]) indices.clone(), indices, 0, indices.length);
      //System.out.println("Compares: "+compares);
   }
   
   private void checkModelo() 
   { if (indices.length!=modelo.getRowCount()) {System.err.println("ERROR el modelo ha cambiado!!!");}}

   
   //==================
   //ordenacionMetodoN2
   //==================
   private void ordenacionMetodoN2() 
   {
      for (int i=0; i<getRowCount(); i++) 
      {
	 for (int j=i+1; j<getRowCount(); j++) 
	 { if (compararFilas(indices[i], indices[j])==-1) { swap(i,j); }}
      }
   }
   private void swap(int i, int j) { int tmp=indices[i];indices[i]=indices[j];indices[j]=tmp;}

   
   //=======================
   //ordenacionMetodoShuttle
   //=======================
   private void ordenacionMetodoShuttle(int from[], int to[], int low, int high) 
   {
      if (high-low<2) {return;}
      int middle = (low+high)/2;
      ordenacionMetodoShuttle(to, from, low, middle);
      ordenacionMetodoShuttle(to, from, middle, high);

      int p=low;
      int q=middle;

      if (high-low >= 4 && compararFilas(from[middle-1], from[middle]) <= 0) 
      { for (int i=low; i<high; i++) {  to[i]=from[i]; }  return; }

      for (int i=low; i<high; i++) 
      {
	 if (q>=high || (p<middle && compararFilas(from[p],from[q]) <=0)) { to[i]=from[p++]; }
         else { to[i]=from[q++]; }
      }
   }

   
   //=============
   //compararFilas
   //=============
   private int compararFilas (int fila1, int fila2) 
   {
     numComparaciones++;
     for (int i=0; i<columnasOrdenadas.size(); i++) 
     {
        Integer columna = (Integer)columnasOrdenadas.elementAt(i);
        int     resultado = compararColumna(fila1, fila2, columna.intValue());
        if (resultado!=0) { return ascendente ? resultado : -resultado;  }
     }
     return 0;
   }

   //================
   //compararColumnas
   //================
   private int compararColumna(int fila1, int fila2, int col) 
   {
      Class tipo = modelo.getColumnClass(col);
      TableModel dato = modelo;
      // Check for nulls.
      Object o1 = dato.getValueAt(fila1, col);
      Object o2 = dato.getValueAt(fila2, col); 

      if (o1==null && o2==null) {return 0;}  // If both values are null, return 0.
      else if (o1==null) { return -1; }	     // Define null less than everything. 
      else if (o2==null) {return 1;}

      if (tipo.getSuperclass()==java.lang.Number.class) 
      {
	 Number n1 = (Number)dato.getValueAt(fila1, col);   double d1 = n1.doubleValue();
	 Number n2 = (Number)dato.getValueAt(fila2, col);   double d2 = n2.doubleValue();

         if (d1<d2) { return -1; } 
	 else if (d1>d2) { return 1;} 
	 else {return 0;}
      } 
      else if (tipo==java.util.Date.class) 
      {
         Date d1 = (Date)dato.getValueAt(fila1, col); long n1 = d1.getTime();
         Date d2 = (Date)dato.getValueAt(fila2, col); long n2 = d2.getTime();

         if (n1<n2)	   { return -1;}
	 else if (n1>n2)   { return 1;} 
	 else		   { return 0;}
      }
      else if (tipo==String.class) 
      {
	 String s1 = (String)dato.getValueAt(fila1, col);
         String s2 = (String)dato.getValueAt(fila2, col);
         int resultado = s1.compareTo(s2);

         if (resultado<0)	 { return -1; } 
	 else if (resultado>0)	 { return 1;  } 
	 else			 { return 0;  }
      } 
      else if (tipo==Boolean.class) 
      {
         Boolean bool1 = (Boolean)dato.getValueAt(fila1, col); boolean b1 = bool1.booleanValue();
         Boolean bool2 = (Boolean)dato.getValueAt(fila1, col); boolean b2 = bool2.booleanValue();

         if (b1==b2)	{ return 0;} 
	 else if (b1)	{ return 1;}  // false < true
	 else		{ return -1; }
      } 
      else 
      {
         Object v1=dato.getValueAt(fila1, col);  String s1 = v1.toString();
         Object v2=dato.getValueAt(fila2, col);  String s2 = v2.toString();
         int resultado=s1.compareTo(s2);

         if (resultado<0)	 { return -1; } 
	 else if (resultado>0)	 { return 1; } 
	 else			 {return 0; }
      }
   }
}