package com.msl.micronaut.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sample DTO object. <b>Please remove for actual project implementation.</b>
 *
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDTO<T> extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "Content of the page", required = true)
	private List<T> content;

	@Schema(description = "Number of elements in the page", required = true)
	private long number;

	@Schema(description = "Size of elements in the page", required = true)
	private long size;

	@Schema(description = "Total elements in the repository", required = true)
	private long totalElements;

	@Schema(description = "Total pages in the repository", required = true)
	private long totalPages;

}
