IF OBJECT_ID(N'dbo.CarRental', N'U') IS NOT NULL
    UPDATE dbo.CarRental
    SET Status = 'ACTIVE'
    WHERE Status IN ('WAITING_FOR_PICKUP', 'WAITING');

IF OBJECT_ID(N'dbo.CarRental', N'U') IS NOT NULL
    UPDATE dbo.CarRental
    SET Status = 'RENTING'
    WHERE Status = 'PENDING';

IF OBJECT_ID(N'dbo.CarRental', N'U') IS NOT NULL
    UPDATE dbo.CarRental
    SET Status = 'ACTIVE'
    WHERE Status IS NULL;

IF OBJECT_ID(N'dbo.CarRental', N'U') IS NOT NULL
    EXEC(N'ALTER TABLE dbo.CarRental ALTER COLUMN Status NVARCHAR(10) NOT NULL');

IF OBJECT_ID(N'dbo.Car', N'U') IS NOT NULL
    UPDATE dbo.Car
    SET Status = 'AVAILABLE'
    WHERE Status = 'MAINTENANCE';

IF OBJECT_ID(N'dbo.Car', N'U') IS NOT NULL
    UPDATE dbo.Car
    SET Status = 'AVAILABLE'
    WHERE Status IS NULL;

IF OBJECT_ID(N'dbo.Car', N'U') IS NOT NULL
    EXEC(N'ALTER TABLE dbo.Car ALTER COLUMN Status NVARCHAR(10) NOT NULL');
