USE [FUCarRentingSystem_DB]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

/****** Object:  Table [dbo].[Account] ******/
CREATE TABLE [dbo].[Account](
    [AccountID] [int] IDENTITY(1,1) NOT NULL,
    [AccountName] [nvarchar](100) NOT NULL,
    [Email] [varchar](200) NOT NULL,
    [Password] [varchar](200) NOT NULL,
    [Role] [nvarchar](10) NOT NULL,
    CONSTRAINT [PK_Account] PRIMARY KEY CLUSTERED ([AccountID] ASC)
) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[CarProducer] ******/
CREATE TABLE [dbo].[CarProducer](
    [ProducerID] [int] IDENTITY(1,1) NOT NULL,
    [ProducerName] [nvarchar](100) NOT NULL,
    [Address] [nvarchar](200) NOT NULL,
    [Country] [nvarchar](100) NOT NULL,
    CONSTRAINT [PK_CarProducer] PRIMARY KEY CLUSTERED ([ProducerID] ASC)
) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[Car] ******/
CREATE TABLE [dbo].[Car](
    [CarID] [int] IDENTITY(1,1) NOT NULL,
    [CarName] [nvarchar](200) NOT NULL,
    [CarModelYear] [int] NOT NULL,
    [Color] [nvarchar](50) NOT NULL,
    [Capacity] [int] NOT NULL,
    [Description] [nvarchar](1000) NOT NULL,
    [ImportDate] [date] NOT NULL,
    [ProducerID] [int] NOT NULL,
    [RentPrice] [decimal](10) NOT NULL,
    [Status] [nvarchar](10) NOT NULL,
    CONSTRAINT [PK_Car] PRIMARY KEY CLUSTERED ([CarID] ASC)
) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[Customer] ******/
CREATE TABLE [dbo].[Customer](
    [CustomerID] [int] IDENTITY(1,1) NOT NULL,
    [FullName] [nvarchar](200) NOT NULL,
    [Mobile] [varchar](15) NOT NULL,
    [Birthday] [date] NOT NULL,
    [IdentityCard] [varchar](20) NOT NULL,
    [LicenceNumber] [varchar](20) NOT NULL,
    [LicenceDate] [date] NOT NULL,
    [AccountID] [int] NOT NULL,
    CONSTRAINT [PK_Customer] PRIMARY KEY CLUSTERED ([CustomerID] ASC)
) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[CarRental] ******/
CREATE TABLE [dbo].[CarRental](
    [CarRenID] [int] IDENTITY(1,1) NOT NULL,
    [CustomerID] [int] NOT NULL,
    [CarID] [int] NOT NULL,
    [PickupDate] [date] NOT NULL,
    [ReturnDate] [date] NOT NULL,
    [RentPrice] [decimal](10) NOT NULL,
    [Status] [nvarchar](10) NOT NULL,
    CONSTRAINT [PK_CarRental] PRIMARY KEY CLUSTERED ([CarRenID] ASC)
) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[Review] ******/
CREATE TABLE [dbo].[Review](
    [ID] [int] IDENTITY(1,1) NOT NULL,
    [CarRenID] [int] NOT NULL,
    [ReviewStar] [int] NOT NULL,
    [Comment] [nvarchar](500) NOT NULL,
    CONSTRAINT [PK_Review] PRIMARY KEY CLUSTERED ([ID] ASC)
) ON [PRIMARY]
GO

/****** Add constraints and foreign keys to Car ******/
ALTER TABLE [dbo].[Car] WITH CHECK ADD CONSTRAINT [FK_Car_CarProducer] FOREIGN KEY([ProducerID])
REFERENCES [dbo].[CarProducer] ([ProducerID])
GO
ALTER TABLE [dbo].[Car] CHECK CONSTRAINT [FK_Car_CarProducer]
GO

/****** Add constraints and foreign keys to Customer ******/
ALTER TABLE [dbo].[Customer] WITH CHECK ADD CONSTRAINT [FK_Customer_Account] FOREIGN KEY([AccountID])
REFERENCES [dbo].[Account] ([AccountID])
GO
ALTER TABLE [dbo].[Customer] CHECK CONSTRAINT [FK_Customer_Account]
GO

/****** Add constraints and foreign keys to CarRental ******/
ALTER TABLE [dbo].[CarRental] WITH CHECK ADD CONSTRAINT [FK_CarRental_Car] FOREIGN KEY([CarID])
REFERENCES [dbo].[Car] ([CarID])
GO
ALTER TABLE [dbo].[CarRental] CHECK CONSTRAINT [FK_CarRental_Car]
GO

ALTER TABLE [dbo].[CarRental] WITH CHECK ADD CONSTRAINT [FK_CarRental_Customer] FOREIGN KEY([CustomerID])
REFERENCES [dbo].[Customer] ([CustomerID])
GO
ALTER TABLE [dbo].[CarRental] CHECK CONSTRAINT [FK_CarRental_Customer]
GO

ALTER TABLE [dbo].[CarRental] WITH CHECK ADD CONSTRAINT [CHK_ReturnDate] CHECK ([ReturnDate] > [PickupDate])
GO
ALTER TABLE [dbo].[CarRental] CHECK CONSTRAINT [CHK_ReturnDate]
GO

/****** Add constraints and foreign keys to Review ******/
ALTER TABLE [dbo].[Review] WITH CHECK ADD CONSTRAINT [FK_Review_CarRental] FOREIGN KEY([CarRenID])
REFERENCES [dbo].[CarRental] ([CarRenID])
GO
ALTER TABLE [dbo].[Review] CHECK CONSTRAINT [FK_Review_CarRental]
GO

ALTER TABLE [dbo].[Review] WITH CHECK ADD CONSTRAINT [CHK_ReviewStar] CHECK ([ReviewStar] >= 1 AND [ReviewStar] <= 5)
GO
ALTER TABLE [dbo].[Review] CHECK CONSTRAINT [CHK_ReviewStar]
GO