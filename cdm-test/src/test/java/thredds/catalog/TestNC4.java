package thredds.catalog;

import java.io.IOException;

import java.util.List;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.iosp.hdf5.H5header;
import ucar.unidata.io.RandomAccessFile;

/**
 * Created by Fei Hu on 2/3/16.
 */
public class TestNC4 {

  public static void main(String[] args) throws IOException, InvalidRangeException {
    //String fPath = "/Users/feihu/Documents/Data/Merra/MERRA300.prod.simul.tavgM_2d_mld_Nx.201306.hdf";
    String fPath = "/Users/feihu/Documents/Data/Merra2/MERRA2_100.inst1_2d_int_Nx.19800101.nc4";

    RandomAccessFile randomAccessFile = new RandomAccessFile(fPath, "rws");
    randomAccessFile.setDebugAccess(true);

    NetcdfFile ncfile = NetcdfFile.open(randomAccessFile, fPath);

    //System.out.println(ncfile.getDetailInfo());
    List<Variable> varList = ncfile.getVariables();
    //for (Variable var : varList) {
    Variable var = varList.get(10);
    int[] start = new int[] {0,0,0};
    int[] shape = new int[] {1,1,1};
    //H5header.Vinfo vinfo = (H5header.Vinfo) var.getSPobject();
    //int[] tmp = vinfo.getChunking();
    Array value = var.read();
    value.getShape();
    //var.getVarLocationInformation();
    //System.out.println(var.getShortName()+ "+++++++++" + var.read(start, shape).getSize() + " , " + var.getVarLocationInformation());
    String varInfo = var.getVarLocationInformation();
    String[] chunks = varInfo.split(";");
    for (String chunk : chunks ) {
      int tt = chunk.indexOf("size=");
      System.out.println(chunk + "\r");
      System.out.println(tt + "\r");
    }
    //}
  }

}
