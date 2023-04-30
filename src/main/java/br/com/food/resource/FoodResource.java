package br.com.food.resource;

import java.util.List;

import br.com.food.controller.FoodController;
import br.com.food.entity.Food;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/food")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FoodResource {

	@Inject
	private FoodController foodController;

	@GET
	public List<Food> findAll() {
		return Food.listAll();
	}

	@POST
	@Transactional
	public Response create(Food food) {
		Food.persist(food);
		return Response.ok(food).status(201).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public Response update(@PathParam("id") Long id, Food food) {

		if (foodController.isFoodNameIsNotEmpty(food)) {
			return Response.ok("Food was not found").type(MediaType.APPLICATION_JSON_TYPE).build();
		}

		Food foodEntity = foodController.update(id, food);

		return Response.ok(foodEntity).build();
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public Response delete(@PathParam("id") Long id) {
		Food foodEntity = Food.findById(id);

		if (foodEntity == null) {
			throw new WebApplicationException("Food with id " + id + " does not exist.", Response.Status.NOT_FOUND);
		}

		foodEntity.delete();
		return Response.status(204).build();
	}
}
