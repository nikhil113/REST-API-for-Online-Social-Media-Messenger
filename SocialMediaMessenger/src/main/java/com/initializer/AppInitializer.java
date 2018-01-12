package com.initializer;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/webapi")
public class AppInitializer extends Application{
	
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classSet=new HashSet<>();

		Set<Class<?>> resourcesClassSet=new HashSet<>();
		Set<Class<?>> exceptionMapperClassSet=new HashSet<>();
		Set<Class<?>> filterClassSet=new HashSet<>();
		
		resourcesClassSet.add(com.resources.ProfileResource.class);
		resourcesClassSet.add(com.resources.TokenResource.class);
		resourcesClassSet.add(com.resources.MessageResourceLinkedWithProfile.class);
		resourcesClassSet.add(com.resources.MessageResource.class);
		
		exceptionMapperClassSet.add(com.exception_mappers.InvalidEmailIdExceptionMapper.class);
		exceptionMapperClassSet.add(com.exception_mappers.UnAuthorizedExceptionMapper.class);
		exceptionMapperClassSet.add(com.exception_mappers.DataNotFoundExceptionMapper.class);
		exceptionMapperClassSet.add(com.exception_mappers.ForbiddenExceptionMapper.class);
		
		filterClassSet.add(com.filter.AuthenticationFilter.class);
		
		classSet.addAll(resourcesClassSet);
		classSet.addAll(exceptionMapperClassSet);
		classSet.addAll(filterClassSet);
		
		return classSet;
	}

}
