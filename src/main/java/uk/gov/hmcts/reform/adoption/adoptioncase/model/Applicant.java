package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.CollectionAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.Email;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class Applicant {

    @CCD(label = "First names")
    private String firstName;

    @CCD(label = "Last names")
    private String lastName;

    @CCD(
        label = "Email address",
        typeOverride = Email
    )
    private String email;

    @CCD(
        label = "Additional Name",
        access = {DefaultAccess.class}
    )
    private YesOrNo hasOtherNames;

    @CCD(
        label = "Previous names",
        typeOverride = Collection,
        typeParameterOverride = "AdditionalName",
        access = {CollectionAccess.class}
    )
    private List<ListValue<AdditionalName>> additionalNames;

    @CCD(
        label = "Date of birth",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @CCD(label = "Occupation")
    private String occupation;

    @CCD(
        label = "Email address",
        typeOverride = Email
    )
    private String emailAddress;

    @CCD(label = "Phone number")
    private String phoneNumber;

    @CCD(label = "The court may want to use your email to serve you court orders. Are you happy to be served court orders by email?")
    private YesOrNo contactDetailsConsent;

    @CCD(label = "Nationality")
    private SortedSet<Nationality> nationality;

    @CCD(
        label = "Additional Nationalities",
        typeOverride = Collection,
        typeParameterOverride = "OtherNationality",
        access = {CollectionAccess.class}
    )
    private List<ListValue<OtherNationality>> additionalNationalities;

    @CCD(label = "Building and street")
    private String address1;

    @CCD(label = "Address2")
    private String address2;

    @CCD(label = "Town or city")
    private String addressTown;

    @CCD(label = "County")
    private String addressCountry;

    @CCD(label = "Postcode")
    private String addressPostCode;

    @CCD(label = "Address same as applicant1")
    private String addressSameAsApplicant1;

    @CCD(label = "contactDetails")
    private Set<ContactDetails> contactDetails;

    @CCD(label = "languagePreference")
    private LanguagePreference languagePreference;

    // Bulk Scan phase2 changes starts

    @CCD(
        label = "Gender",
        hint = "Applicant Gender",
        typeOverride = FixedList,
        typeParameterOverride = "Gender"
    )
    private Gender gender;

    @CCD(
        label = "Applicant Domicile Status",
        access = {DefaultAccess.class}
    )
    private String domicileStatus;

    @CCD(label = "relationToChild")
    private String relationToChild;


    @CCD(
        label = "Applicant Marital Status",
        access = {DefaultAccess.class}
    )
    private String maritalStatus;

    @CCD(
        label = "Applicant or Legal Representative Signature",
        access = {DefaultAccess.class}
    )
    private String signStatementOfTruth;

    @CCD(
        label = "Applicant or Legal Representative Signature ",
        access = {DefaultAccess.class}
    )
    private String legalRepSignature;

    @CCD(
        label = "Applicant Signing",
        access = {DefaultAccess.class}
    )
    private String signing;

    @CCD(
        label = "Legal Representative Signing",
        access = {DefaultAccess.class}
    )
    private String legalRepSigning;

    @CCD(
        label = "Applicant Statement of Truth Date",
        access = {DefaultAccess.class}
    )
    private String statementOfTruthDate;

    @CCD(
        label = "Name of first applicant's legal representatives firm",
        access = {DefaultAccess.class}
    )
    private String legalRepFirm;

    @CCD(
        label = "firm or company give position or office held",
        access = {DefaultAccess.class}
    )
    private String legalRepPosition;

    @CCD(
        label = "Applicant Requires Interpreter",
        access = {DefaultAccess.class}
    )
    private String requireInterpreter;


    // BULK SCAN phase 2 changes ends
}
