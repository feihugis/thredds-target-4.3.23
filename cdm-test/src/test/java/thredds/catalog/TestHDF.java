package thredds.catalog;

import ucar.ma2.*;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.iosp.hdf4.H4header;
import ucar.nc2.iosp.netcdf3.N3header;


import java.io.IOException;
import java.util.List;

/**
 * Created by feihu on 9/11/14.
 */
public class TestHDF {

    public static void main(String args[]) throws IOException{

        //NetcdfFile ncfile = NetcdfFile.open("/Users/feihu/Documents/Data/Merra/MERRA300.prod.simul.tavgM_2d_mld_Nx.200901.hdf");///Users/feihu/Documents/Data/Merra100S/MERRA300.prod.simul.tavg1_2d_mld_Nx.20140103.hdf
        NetcdfFile ncfile = NetcdfFile.open("/Users/feihu/Documents/Data/Merra100S/MERRA300.prod.simul.tavg1_2d_mld_Nx.20141003.hdf");
        Variable var = ncfile.getVariables().get(6);//6
        List<Variable>varList = ncfile.getVariables();

        for (Variable variable : varList) {
            if (variable.getShortName().equals("LAI")) {
                var = variable;
            }

            //System.out.println(ss.toString());
            if (variable.isMetadata()) {
                System.out.println("MetaData -- " + variable.getShortName() + "\n");
            } else if( variable.isCoordinateVariable()) {
                System.out.println("CoordinateVariable -- " + variable.getShortName() + "\n");
            } else if ( variable.getGroup().getShortName().contains("Data_Fields")) {
                try {
                    System.out.println(variable.getShortName() + "  " + variable.getVarLocationInformation() + "\n");
                } catch (InvalidRangeException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }


        int[] origin = new int[var.getRank()];
        int[] shape = new int[var.getShape().length];

        for (int i=0; i<origin.length; i++) {
            origin[i] = 0;
            shape[i] = 1; //var.getDimension(i).getLength()-origin[i];
        }

        origin[0] = 0;
        origin[1] = 310;
        origin[2] = 0;

        String dataString = new String();

        System.out.println(var.getShortName());

        try {
            //ArrayLong offset = var.getLocalityInformation(origin,shape);

            //ArrayFloat data = (ArrayFloat) var.read(origin, shape);

            String info = var.getVarLocationInformation();

            System.out.println(info);

            /*for (int i=309; i<330; i++) {
                for (int j=0; j<100; j++) {
                    dataString = dataString + data.getFloat(i*361 + j) + "  ";
                }
                dataString = dataString + "\n";
            }*/

            //System.out.println("Value = " + data.getFloat(0) + " Location = " + offset.getLong(0));
          //long length = offset.getSize();
          //long num = offset.getLong(0);
          //System.out.println(num);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidRangeException e) {
            e.printStackTrace();
        }

    }

}
