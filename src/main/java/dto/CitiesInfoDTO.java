
package dto;

import entities.CityInfo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cahit
 */
public class CitiesInfoDTO {
    
  List<CityInfoDTO> citiesInfo = new ArrayList();  
    
  public CitiesInfoDTO(List<CityInfo> cityInfoes ){
      for (CityInfo cityInfo : cityInfoes) {
         citiesInfo.add(new CityInfoDTO(cityInfo));
      }
  }

    public List<CityInfoDTO> getCitiesInfo() {
        return citiesInfo;
    }

    public void setCitiesInfo(List<CityInfoDTO> citiesInfo) {
        this.citiesInfo = citiesInfo;
    }
    
}
