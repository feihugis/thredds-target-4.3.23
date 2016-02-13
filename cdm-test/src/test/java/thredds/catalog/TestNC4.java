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

    RandomAccessFile randomAccessFile = new RandomAccessFile("/Users/feihu/Documents/Data/Merra2/MERRA2_100.inst1_2d_int_Nx.19800101.nc4",
                                                             "rws");
    randomAccessFile.setDebugAccess(true);

    NetcdfFile ncfile = NetcdfFile.open(randomAccessFile, "/Users/feihu/Documents/Data/Merra2/MERRA2_100.inst1_2d_int_Nx.19800101.nc4");

    //System.out.println(ncfile.getDetailInfo());
    List<Variable> varList = ncfile.getVariables();
    //for (Variable var : varList) {
    Variable var = varList.get(10);
    int[] start = new int[] {0,0,0};
    int[] shape = new int[] {10,100,100};
    H5header.Vinfo vinfo = (H5header.Vinfo) var.getSPobject();
    int[] tmp = vinfo.getChunking();
    Array value = var.read();
    value.getShape();
    System.out.println(var.getShortName()+ vinfo.getChunking().length + "+++++++++" + var.read(start, shape).getSize() + " , " + value.getShape()[0]);
    //}
  }

}
