//
//  AnalizDataUserPage.h
//  takeda
//
//  Created by Serg on 4/2/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol AnalizDataUserPageDelegate;

@interface AnalizDataUserPage : VControllerExt

@property int page;
@property (nonatomic,retain) IBOutlet UIButton *nextStepBtn;
@property (nonatomic,retain) NSArray *sourceData;
@property (nonatomic,retain) IBOutlet UITableView *tableView;
@property (assign) __unsafe_unretained id <AnalizDataUserPageDelegate> delegate;
-(void)reloadData;
@end

@protocol AnalizDataUserPageDelegate <NSObject>
-(void)analizDataUserPage:(AnalizDataUserPage*)analizPage openList:(NSString*)type sourceData:(NSMutableDictionary*)sd;
@end
