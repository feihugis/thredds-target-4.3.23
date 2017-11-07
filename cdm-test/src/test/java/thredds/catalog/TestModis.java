package thredds.catalog;

import java.io.IOException;

import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 * Created by Fei Hu on 8/24/16.
 */
public class TestModis {
  public static void main(String[] args) throws IOException {
    //String fPath = "/Users/feihu/Documents/Data/Merra/MERRA300.prod.simul.tavgM_2d_mld_Nx.201306.hdf";
    String fPath = "/Users/feihu/Documents/Data/modis_hdf/MOD08_D3/MOD08_D3.A2016001.006.2016008061022.hdf";
    NetcdfFile ncFile = NetcdfFile.open(fPath);
    Variable variable = ncFile.getVariables().get(235);
    variable.read();
    try {
      System.out.println(variable.getVarLocationInformation());
    } catch (InvalidRangeException e) {
      e.printStackTrace();
    }
  }
}
