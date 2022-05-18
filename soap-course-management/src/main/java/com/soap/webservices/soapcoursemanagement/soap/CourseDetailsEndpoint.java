package com.soap.webservices.soapcoursemanagement.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.in28minutes.courses.CourseDetails;
import com.in28minutes.courses.DeleteCourseDetailsRequest;
import com.in28minutes.courses.DeleteCourseDetailsResponse;
import com.in28minutes.courses.GetAllCourseDetailsRequest;
import com.in28minutes.courses.GetAllCourseDetailsResponse;
import com.in28minutes.courses.GetCourseDetailsRequest;
import com.in28minutes.courses.GetCourseDetailsResponse;
import com.soap.webservices.soapcoursemanagement.soap.bean.Course;
import com.soap.webservices.soapcoursemanagement.soap.exception.CourseNotFoundException;
import com.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService;
import com.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService.Status;

// How we can tell to Spring Boot that this is an Endpoint?
// We use the annotation @Endpoint
// and
// this accept request and send a response
@Endpoint
public class CourseDetailsEndpoint {
	
	@Autowired
	CourseDetailsService service; // after we have autowired the service, we have to call it in the payload down here
		// we have created these objs to test our SOAP app

	// We want to create a method that has:
	// 1) input - request [GetCourseDetailsRequest]
	// We want to create a method which would take the input [to do it, we pass it as parameter]
	// 2) output - response [GetCourseDetailsResponse]
	// as the request object and give the response as the response object
	
	// We use the PayloadRoot annotation
	// to say that in the .xsd file we want to support
	// the request of the xmlns:tns that in our case is: "http://in28minutes.com/courses"
	// and the request what it would get? Well, it's the type GetCourseDetailsRequest
	
	// So if a request comes with a namespace and this GetCourseDetailsRequest
	// name(element name:)
	
	// How can we say to Spring Boot Web Services
	// to process any request with this namespace and this name?
	// Is with the @PayloadRoot annotation
	@PayloadRoot(namespace = "http://in28minutes.com/courses",
				 localPart = "GetCourseDetailsRequest")
	@ResponsePayload // we use response payload to convert the response from Java to XML
		// so before we get the Request, we convert it with the @RequestPayload annotation
		// and then we convert the return response from Java to XML again
	public GetCourseDetailsResponse processCourseDetailsRequest
		(@RequestPayload GetCourseDetailsRequest request) {
		// Another thing we want to do is to take the XML and map it to this Java class
		// so to convert the XML request in Java
		// we use as parameter the @RequestPayload annotation
		
		Course course = service.findById(request.getId());
		
//		if(course == null)
//			throw new RuntimeException("Invalid Course Id " + request.getId() );
		
		if(course == null)
			throw new CourseNotFoundException("Invalid Course Id " + request.getId() );
		
		// instead of putting all the business logic to retrieve the data
		// we have done a refactor to have a new method that we will return here
		// for a cleaner code
		
		// =========================================================================
//		GetCourseDetailsResponse response = new GetCourseDetailsResponse();
//		CourseDetails courseDetails = new CourseDetails();
//		courseDetails.setId(course.getId());
//		courseDetails.setName(course.getName());
//		courseDetails.setDescription(course.getDescription());
		
//		courseDetails.setId(request.getId());
//		courseDetails.setName("Microservice Course");
//		courseDetails.setDescription("That would be a wonderful course");
		
//		response.setCourseDetails(courseDetails);
//		return response;
		// =========================================================================
		
		return mapCourseDetails(course);
	}
	
	private GetCourseDetailsResponse mapCourseDetails(Course course) {
		GetCourseDetailsResponse response = new GetCourseDetailsResponse();
		response.setCourseDetails(mapCourse(course));
		return response;
	}

	// we create a separate method to re-use courseDetails that we need for the findAll()
	private CourseDetails mapCourse(Course course) {
		CourseDetails courseDetails = new CourseDetails();
		courseDetails.setId(course.getId());
		courseDetails.setName(course.getName());
		courseDetails.setDescription(course.getDescription());
		return courseDetails;
	}

	// So with this class we have created an Endpoint with hardcorded logic
	// and this is not a good thing to do.
	// So to solve this we will create a new Bean class called Course.
	
	// ==================================================================================
	
	// we copy and paste the previous method to GetAll the Course Details Request
	@PayloadRoot(namespace = "http://in28minutes.com/courses", localPart = "GetAllCourseDetailsRequest")
	@ResponsePayload
	public GetAllCourseDetailsResponse processAllCourseDetailsRequest(@RequestPayload GetAllCourseDetailsRequest request) {
		List<Course> courses = service.findAll();
		return mapAllCourseDetails(courses);
	}
	
	private GetAllCourseDetailsResponse mapAllCourseDetails(List<Course> courses) {
		GetAllCourseDetailsResponse response = new GetAllCourseDetailsResponse();
		for(Course course : courses) { // here we are looping all the courses
			CourseDetails mapCourse = mapCourse(course); // mapping each course
			response.getCourseDetails().add(mapCourse); // then we add the course details in getCourseDetails()
		}
		return response;
	}
	
	// ==================================================================================

	@PayloadRoot(namespace = "http://in28minutes.com/courses", localPart = "DeleteCourseDetailsRequest")
	@ResponsePayload
	public DeleteCourseDetailsResponse deleteCourseDetailsRequest(@RequestPayload DeleteCourseDetailsRequest request) {
		Status status = service.deleteById(request.getId());
		DeleteCourseDetailsResponse response = new DeleteCourseDetailsResponse();
		response.setStatus(mapStatus(status)); // the setStatus comes from the DeleteCourseDetailsResponse
		return response;
	}

	private com.in28minutes.courses.Status mapStatus(Status status) {
		if(status == Status.FAILURE)
			return com.in28minutes.courses.Status.FAILURE;
		return com.in28minutes.courses.Status.SUCCESS;
	}
	
}
