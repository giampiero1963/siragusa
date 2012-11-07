package ellematica.server


class StatisticsController {
	StatisticsService statisticsService
	
	def statistics={
		statisticsService.statistics()
		render "ok"
	}
	

  
}