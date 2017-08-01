USE [Questions]
GO
/****** Object:  Table [dbo].[Correct_Answers]    Script Date: 18.06.2017 19:28:10 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Correct_Answers](
	[Correct_Answer_ID] [smallint] IDENTITY(1,1) NOT NULL,
	[Question_ID] [smallint] NULL,
	[Correct_Answer] [nvarchar](100) NOT NULL,
 CONSTRAINT [PK_Correct_Answers] PRIMARY KEY CLUSTERED 
(
	[Correct_Answer_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Incorrect_Answers]    Script Date: 18.06.2017 19:28:10 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Incorrect_Answers](
	[Incorrect_Answer_ID] [smallint] IDENTITY(1,1) NOT NULL,
	[Question_ID] [smallint] NULL,
	[Incorrect_Answer] [nvarchar](100) NOT NULL,
 CONSTRAINT [PK_Incorrect_Answers] PRIMARY KEY CLUSTERED 
(
	[Incorrect_Answer_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Questions]    Script Date: 18.06.2017 19:28:10 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Questions](
	[Question_ID] [smallint] IDENTITY(1,1) NOT NULL,
	[Question] [nvarchar](200) NOT NULL,
 CONSTRAINT [PK_Questions] PRIMARY KEY CLUSTERED 
(
	[Question_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Correct_Answers]  WITH CHECK ADD  CONSTRAINT [FK_Correct_Answers_Questions] FOREIGN KEY([Question_ID])
REFERENCES [dbo].[Questions] ([Question_ID])
GO
ALTER TABLE [dbo].[Correct_Answers] CHECK CONSTRAINT [FK_Correct_Answers_Questions]
GO
ALTER TABLE [dbo].[Incorrect_Answers]  WITH CHECK ADD  CONSTRAINT [FK_Incorrect_Answers_Questions] FOREIGN KEY([Question_ID])
REFERENCES [dbo].[Questions] ([Question_ID])
GO
ALTER TABLE [dbo].[Incorrect_Answers] CHECK CONSTRAINT [FK_Incorrect_Answers_Questions]
GO
