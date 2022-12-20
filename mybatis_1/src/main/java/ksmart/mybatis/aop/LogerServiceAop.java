package ksmart.mybatis.aop;

import java.util.StringJoiner;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class LogerServiceAop {
	
	private static final Logger log = LoggerFactory.getLogger(LogerServiceAop.class);

	   /**
	   * Pointcut : 적용할 지점 또는 범위 
	   * 예시)
	   * execution(Example get*(..))
	   * 리턴 타입이 Example이고, 메서드의 이름이 get으로 시작하고, 파라미터가 0개 이상인 모든 메서드가 호출될 때
	   * execution(* ksmart.spring.controller.*())
	   * 해당 패키지 밑에 파라미터가 없는 모든 메서드가 호출될 때
	   * execution(* ksmart.mybatis.controller.*(..))
	   * 해당 패키지 밑에 파라미터가 0개 이상인 모든 메서드가 호출될 때
	   * execution(* ksmart.spring..get(*))
	   * ksmart.spring 패키지의 모든 하위 패키지에 존재하는 get으로 시작하고, 파라미터가 한 개인 모든 메서드가 호출될 때
	   * execution(* ksmart.mybatis..get(*, *))
	   * ksmart.spring 패키지의 모든 하위 패키지에 존재하는 get으로 시작하고, 파라미터가 두 개인 모든 메서드가 호출될 때
	   */
	   @Pointcut("execution(* ksmart.mybatis.service.*Service.*(..))")	
	   private void loggerTarget() {};
	   
	   /**
	    * 
	    * @param joinPoint 어드바이스를 적용하는 지점, 조인포인트는 항상 메서드 실행 단계만 가능
	    * @return joinPoint.proceed(); Object(Service): Aop 공통 모듈의 메소드가 실행된 후 리턴되는 데이터를 받는 메소드
	    * @throws Throwable
	    */
	   @Around("loggerTarget()")
	   public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
		   
		   MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature(); //클라이언트가 호출된 메소드의 시그니처(리턴 타입, 이름, 매개변수) 정보가 저장된 객체를 리턴
		   StringJoiner param = null;
		   
		   String[] paramName = methodSignature.getParameterNames();
		   Object[] argsArr = joinPoint.getArgs();
		   
		   if(paramName != null && argsArr != null) {
			   int paramNameLength = paramName.length;
			   
			   param = new StringJoiner(", ");
			   for(int i = 0; i<paramNameLength; i++) {
				   param.add(paramName[i] = ":" + argsArr[i]);
			   }
		   }
		   
		   log.info("Service Method Info ============================");
		   
		   log.info("Method Name ::::: {}", methodSignature.getName());
		   if(param != null) log.info("Parameter ::::: {}", param);
		   
		   log.info("================================================");
		   
		   return joinPoint.proceed();
	   }
}
