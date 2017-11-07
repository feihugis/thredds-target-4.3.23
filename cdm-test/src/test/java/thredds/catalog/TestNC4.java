package thredds.catalog;

import java.io.IOException;
import java.util.List;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.unidata.io.RandomAccessFile;

/**
 * Created by Fei Hu on 2/3/16.
 */
public class TestNC4 {

  public static void main(String[] args) throws IOException, InvalidRangeException {
    //String fPath = "/Users/feihu/Documents/Data/Merra/MERRA300.prod.simul.tavgM_2d_mld_Nx.201306.hdf";
    String fPath = "/Users/feihu/Documents/Data/M2T1NXINT/1980/01/MERRA2_100.tavg1_2d_int_Nx.19800101.nc4";

    //String fPath = "/Users/fei.hu1@ibm.com/Downloads/test.nc4";

    RandomAccessFile randomAccessFile = new RandomAccessFile(fPath, "rws");
    randomAccessFile.setDebugAccess(true);

    NetcdfFile ncfile = NetcdfFile.open(randomAccessFile, fPath);

    //System.out.println(ncfile.getDetailInfo());
    List<Variable> varList = ncfile.getVariables();
    varList.get(5).read();
    //for (Variable var : varList) {
    Variable var = varList.get(11);

    int[] start = new int[] {0,0,0};
    int[] shape = new int[] {1,1,1};
    Array value = var.read(start, shape);
    //H5header.Vinfo vinfo = (H5header.Vinfo) var.getSPobject();
    //int[] tmp = vinfo.getChunking();
    //Array value = var.read();
    //value.getShape();
    //var.getVarLocationInformation();
    //System.out.println(var.getShortName()+ "+++++++++" + var.read(start, shape).getSize() + " , " + var.getVarLocationInformation());
    String varInfo = var.getVarLocationInformation();
    System.out.println(varInfo);
    int i=0;
    String[] chunks = varInfo.split(";");

    /*for (String chunk : chunks ) {
      int sizeB = chunk.indexOf("size=") + "size=".length();
      int sizeE = chunk.indexOf(" filterMask");
      int size = Integer.parseInt(chunk.substring(sizeB, sizeE));

      int posB = chunk.indexOf("filePos=") + "filePos=".length();
      int posE = chunk.indexOf(" offsets");
      int filePos = Integer.parseInt(chunk.substring(posB, posE));
      //System.out.println(chunk + "\n");

      if (i%2 >0) {
        //System.out.println("" + filePos + " ; End " + (filePos+size) + "----------" + chunk+ "\n");
      } else{
        //System.out.println("" + (filePos+size) + " ; Start: " + filePos + "----------" + chunk+ "\n");
      }
      i++;
    }*/
    //}
  }

}
