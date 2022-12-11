package com.banque.irrigationsystem;

import com.banque.irrigationsystem.modules.land.dto.AddLandRequest;
import com.banque.irrigationsystem.modules.land.dto.LandType;
import com.banque.irrigationsystem.modules.land.service.LandService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.IntStream;

@SpringBootApplication
public class IrrigationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(IrrigationSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(LandService landService) {
		String[] purposeList = {"creation centre","event centre", "picnic", "farming"};
		return (args) -> {
			// save a few land , which automatically creates slot records
			IntStream.range(0, 4).forEach(index -> {
				landService.addLand(new AddLandRequest("REF-0131"+index,
						index+1,
						purposeList[index],
						LandType.GARDEN));
			});
		};
	}

}
