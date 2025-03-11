package data.favourites.model

import com.tuapp.data.favourites.model.DatabaseQuotationDto
import domain.model.Quotation

// Extensión para convertir DatabaseQuotationDto a Quotation
fun DatabaseQuotationDto.toDomain(): Quotation {
    return Quotation(
        id = this.id,
        text = this.text,
        author = this.author
    )
}

// Extensión para convertir Quotation a DatabaseQuotationDto
fun Quotation.toDatabaseDto(): DatabaseQuotationDto {
    return DatabaseQuotationDto(
        id = this.id,
        text = this.text,
        author = this.author
    )
}
