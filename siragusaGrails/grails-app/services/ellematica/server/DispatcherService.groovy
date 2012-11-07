package ellematica.server

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class DispatcherService  implements ApplicationContextAware { 
     ApplicationContext applicationContext 
  

    static transactional = true

    def dispatch(String bean,String method, pars=null) {
			if(pars)
				return applicationContext.getBean(bean)."$method"(*pars)
			else
				return applicationContext.getBean(bean)."$method"()
			
    }
}
